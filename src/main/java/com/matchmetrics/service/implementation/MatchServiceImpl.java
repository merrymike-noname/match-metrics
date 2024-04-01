package com.matchmetrics.service.implementation;

import com.matchmetrics.entity.Match;
import com.matchmetrics.entity.Probability;
import com.matchmetrics.entity.Team;
import com.matchmetrics.entity.dto.match.MatchAddUpdateDto;
import com.matchmetrics.entity.dto.match.MatchMainDto;
import com.matchmetrics.entity.dto.probability.ProbabilityMainDto;
import com.matchmetrics.entity.mapper.match.MatchAddUpdateMapper;
import com.matchmetrics.entity.mapper.match.MatchMainMapper;
import com.matchmetrics.exception.*;
import com.matchmetrics.repository.MatchRepository;
import com.matchmetrics.repository.TeamRepository;
import com.matchmetrics.service.MatchService;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchServiceImpl implements MatchService {

    private final Logger logger = LoggerFactory.getLogger(MatchServiceImpl.class);

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final MatchMainMapper matchMainMapper;
    private final MatchAddUpdateMapper matchAddUpdateMapperMapper;

    @Autowired
    public MatchServiceImpl(MatchRepository matchRepository,
                            TeamRepository teamRepository,
                            MatchMainMapper matchMainMapper,
                            MatchAddUpdateMapper matchAddUpdateMapperMapper) {
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.matchMainMapper = matchMainMapper;
        this.matchAddUpdateMapperMapper = matchAddUpdateMapperMapper;
    }

    @Override
    public List<MatchMainDto> getAllMatches(
        Integer page, Integer perPage, String sortBy
    ) {
        Pageable pageable = createPageable(page, perPage, sortBy);

        return matchRepository.findAll(pageable).getContent().stream()
                .map(matchMainMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<MatchMainDto> getMatchesByCriteria(
            String team, Boolean isHome, String date, String league,
            Integer page, Integer perPage, String sortBy
    ) {
        Pageable pageable = createPageable(page, perPage, sortBy);
        Specification<Match> spec = createSpecification(team, isHome, date, league);
        Page<Match> matches = matchRepository.findAll(spec, pageable);
        return matches.getContent().stream()
                .map(matchMainMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public MatchMainDto getMatchById(int id) {
        Optional<Match> match = matchRepository.findById(id);
        if (match.isEmpty()) {
            logger.error("Match with ID {} not found", id);
            throw new MatchDoesNotExistException(id);
        }
        return matchMainMapper.toDto(match.get());
    }

    @Override
    public MatchMainDto addMatch(MatchAddUpdateDto matchDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append("\n"));
            logger.error("Error occurred while adding match: {}", errorMessage);
            throw new InvalidDataException(errorMessage.toString());
        }
        Team homeTeam = teamRepository.findTeamByName(
                        matchDto.getHomeTeam().getName())
                .orElseThrow(() -> new TeamDoesNotExistException(matchDto.getHomeTeam().getName()));
        Team awayTeam = teamRepository.findTeamByName(
                        matchDto.getAwayTeam().getName())
                .orElseThrow(() -> new TeamDoesNotExistException(matchDto.getAwayTeam().getName()));

        ProbabilityMainDto probabilityDto = matchDto.getProbability();
        Probability probability = new Probability(
                probabilityDto.getHomeTeamWin(),
                probabilityDto.getDraw(),
                probabilityDto.getAwayTeamWin());

        Match matchEntity = matchAddUpdateMapperMapper.toEntity(matchDto);
        matchEntity.setHomeTeam(homeTeam);
        matchEntity.setAwayTeam(awayTeam);
        matchEntity.setProbability(probability);

        homeTeam.getHomeMatches().add(matchEntity);
        awayTeam.getAwayMatches().add(matchEntity);

        return matchMainMapper.toDto(matchRepository.save(matchEntity));
    }

    @Override
    public MatchMainDto updateMatch(int id, MatchAddUpdateDto matchDto, BindingResult bindingResult) {
        if (matchRepository.existsById(id)) {
            if (bindingResult.hasErrors()) {
                StringBuilder errorMessage = new StringBuilder();
                bindingResult.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append("\n"));
                logger.error("Error occurred while updating match: {}", errorMessage);
                throw new InvalidDataException(errorMessage.toString());
            }
            Match existingMatch = matchRepository.findById(id)
                    .orElseThrow(() -> new MatchDoesNotExistException(id));

            Team homeTeam = teamRepository.findTeamByName(
                            matchDto.getHomeTeam().getName())
                    .orElseThrow(() -> new TeamDoesNotExistException(matchDto.getHomeTeam().getName()));
            Team awayTeam = teamRepository.findTeamByName(
                            matchDto.getAwayTeam().getName())
                    .orElseThrow(() -> new TeamDoesNotExistException(matchDto.getAwayTeam().getName()));

            ProbabilityMainDto probabilityDto = matchDto.getProbability();
            Probability probability = new Probability(
                    probabilityDto.getHomeTeamWin(),
                    probabilityDto.getDraw(),
                    probabilityDto.getAwayTeamWin());

            if (existingMatch.getHomeTeam() != homeTeam) {
                existingMatch.getHomeTeam().getHomeMatches().remove(existingMatch);
                homeTeam.getHomeMatches().add(existingMatch);
            }
            if (existingMatch.getAwayTeam() != awayTeam) {
                existingMatch.getAwayTeam().getAwayMatches().remove(existingMatch);
                awayTeam.getAwayMatches().add(existingMatch);
            }

            existingMatch.setDate(matchDto.getDate());
            existingMatch.setLeague(matchDto.getLeague());
            existingMatch.setHomeTeam(homeTeam);
            existingMatch.setAwayTeam(awayTeam);
            existingMatch.setProbability(probability);

            return matchMainMapper.toDto(matchRepository.save(existingMatch));
        } else {
            logger.error("Match with ID {} not found", id);
            throw new MatchDoesNotExistException(id);
        }
    }

    @Override
    public void deleteMatch(int id) {
        matchRepository.deleteById(id);
    }

    public Date convertStringToDate(String strDate) {
        if (strDate == null) {
            return null;
        }
        try {
            String pattern = "yyyy-MM-dd";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
            return simpleDateFormat.parse(strDate);
        } catch (ParseException e) {
            logger.error("Error occurred while converting string to date: {}", e.getMessage());
            throw new DateConversionException(e);
        }
    }

    private Pageable createPageable(int page, int perPage, String sortBy) {
        if (!sortBy.equals("default") && !checkIfFieldExists(sortBy)) {
            logger.error("Error occurred while trying to find a '" + sortBy + "' field in " + Match.class.getName());
            throw new FieldDoesNotExistException(sortBy, Match.class);
        }
        Sort sort = sortBy.equals("default") ? Sort.unsorted() : Sort.by(sortBy);
        return PageRequest.of(page, perPage, sort);
    }

    private boolean checkIfFieldExists(String fieldName) {
        return Arrays.stream(Match.class.getDeclaredFields())
                .anyMatch(field -> field.getName().equals(fieldName));
    }

    private Specification<Match> createSpecification(String team, Boolean isHome, String date, String league) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Match, Team> homeTeam = root.join("homeTeam", JoinType.LEFT);
            Join<Match, Team> awayTeam = root.join("awayTeam", JoinType.LEFT);
            if (team != null) {
                predicates.add(
                        criteriaBuilder.or(
                                criteriaBuilder.equal(homeTeam.get("name"), team),
                                criteriaBuilder.equal(awayTeam.get("name"), team)
                        )
                );
                if (isHome != null) {
                    predicates.clear();
                    if(isHome) {
                        predicates.add(criteriaBuilder.equal(homeTeam.get("name"), team));
                    } else {
                        predicates.add(criteriaBuilder.equal(awayTeam.get("name"), team));
                    }
                }
            }
            if (date != null) {
                predicates.add(criteriaBuilder.equal(root.get("date"), convertStringToDate(date)));
            }
            if (league != null) {
                predicates.add(criteriaBuilder.equal(root.get("league"), league));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
