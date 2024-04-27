package com.matchmetrics.service.implementation;

import com.matchmetrics.entity.Match;
import com.matchmetrics.entity.Probability;
import com.matchmetrics.entity.Team;
import com.matchmetrics.entity.dto.match.MatchAddUpdateDto;
import com.matchmetrics.entity.dto.match.MatchGetDto;
import com.matchmetrics.entity.mapper.match.MatchAddUpdateMapper;
import com.matchmetrics.entity.mapper.match.MatchGetMapper;
import com.matchmetrics.entity.mapper.probability.ProbabilityGetMapper;
import com.matchmetrics.entity.validator.DateValidator;
import com.matchmetrics.entity.validator.ProbabilityValidator;
import com.matchmetrics.exception.*;
import com.matchmetrics.repository.MatchRepository;
import com.matchmetrics.repository.TeamRepository;
import com.matchmetrics.service.MatchService;
import com.matchmetrics.util.PageableCreator;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    private final PageableCreator pageableCreator;
    private final DateValidator dateValidator;
    private final ProbabilityValidator probabilityValidator;

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final MatchGetMapper matchGetMapper;
    private final MatchAddUpdateMapper matchAddUpdateMapperMapper;
    private final ProbabilityGetMapper probabilityGetMapper;

    @Autowired
    public MatchServiceImpl(PageableCreator pageableCreator,
                            DateValidator dateValidator,
                            ProbabilityValidator probabilityValidator,
                            MatchRepository matchRepository,
                            TeamRepository teamRepository,
                            MatchGetMapper matchGetMapper,
                            MatchAddUpdateMapper matchAddUpdateMapperMapper,
                            ProbabilityGetMapper probabilityGetMapper) {
        this.pageableCreator = pageableCreator;
        this.dateValidator = dateValidator;
        this.probabilityValidator = probabilityValidator;
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
        this.matchGetMapper = matchGetMapper;
        this.matchAddUpdateMapperMapper = matchAddUpdateMapperMapper;
        this.probabilityGetMapper = probabilityGetMapper;
    }

    @Override
    public List<MatchGetDto> getAllMatches(
            Integer page, Integer perPage, String sortBy
    ) {
        Pageable pageable = pageableCreator.createPageable(page, perPage, sortBy, Match.class);

        return matchRepository.findAll(pageable).getContent().stream()
                .map(matchGetMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public List<MatchGetDto> getMatchesByCriteria(
            String team, Boolean isHome, String date, String league,
            Integer page, Integer perPage, String sortBy
    ) {
        Pageable pageable = pageableCreator.createPageable(page, perPage, sortBy, Match.class);
        Specification<Match> spec = createSpecification(team, isHome, date, league);
        Page<Match> matches = matchRepository.findAll(spec, pageable);

        if (matches.isEmpty()) {
            logger.warn("No matches found with the given criteria. Team: {}, IsHome: {}, Date: {}, League: {}",
                    team, isHome, date, league);
        }

        return matches.getContent().stream()
                .map(matchGetMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public MatchGetDto getMatchById(int id) {
        Optional<Match> match = matchRepository.findById(id);
        if (match.isEmpty()) {
            logger.error("Match with ID {} not found", id);
            throw new MatchDoesNotExistException(id);
        }
        return matchGetMapper.toDto(match.get());
    }

    @Override
    public MatchGetDto addMatch(MatchAddUpdateDto matchDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            String errorMessage = String.join(", ", errorMessages);
            logger.error("Error occurred while adding match: {}", errorMessage);
            throw new InvalidDataException(errorMessage);
        }

        dateValidator.validate(matchDto.getDate());

        Team homeTeam = teamRepository.findTeamByName(
                        matchDto.getHomeTeam().getName())
                .orElseThrow(() -> new TeamDoesNotExistException(matchDto.getHomeTeam().getName()));
        Team awayTeam = teamRepository.findTeamByName(
                        matchDto.getAwayTeam().getName())
                .orElseThrow(() -> new TeamDoesNotExistException(matchDto.getAwayTeam().getName()));

        probabilityValidator.validateProbability(matchDto.getProbability());
        Probability probability = probabilityGetMapper.toEntity(matchDto.getProbability());

        Match matchEntity = matchAddUpdateMapperMapper.toEntity(matchDto);
        matchEntity.setHomeTeam(homeTeam);
        matchEntity.setAwayTeam(awayTeam);
        matchEntity.setProbability(probability);

        homeTeam.getHomeMatches().add(matchEntity);
        awayTeam.getAwayMatches().add(matchEntity);

        return matchGetMapper.toDto(matchRepository.save(matchEntity));
    }

    @Override
    public MatchGetDto updateMatch(int id, MatchAddUpdateDto matchDto, BindingResult bindingResult) {
        if (matchRepository.existsById(id)) {
            if (bindingResult.hasErrors()) {
                List<String> errorMessages = bindingResult.getAllErrors().stream()
                        .map(DefaultMessageSourceResolvable::getDefaultMessage)
                        .collect(Collectors.toList());
                String errorMessage = String.join(", ", errorMessages);
                logger.error("Error occurred while updating match: {}", errorMessage);
                throw new InvalidDataException(errorMessage);
            }

            dateValidator.validate(matchDto.getDate());

            Match existingMatch = matchRepository.findById(id)
                    .orElseThrow(() -> new MatchDoesNotExistException(id));

            Team homeTeam = teamRepository.findTeamByName(
                            matchDto.getHomeTeam().getName())
                    .orElseThrow(() -> new TeamDoesNotExistException(matchDto.getHomeTeam().getName()));
            Team awayTeam = teamRepository.findTeamByName(
                            matchDto.getAwayTeam().getName())
                    .orElseThrow(() -> new TeamDoesNotExistException(matchDto.getAwayTeam().getName()));

            probabilityValidator.validateProbability(matchDto.getProbability());
            Probability probability = probabilityGetMapper.toEntity(matchDto.getProbability());

            if (existingMatch.getHomeTeam() != homeTeam) {
                existingMatch.getHomeTeam().getHomeMatches().remove(existingMatch);
                homeTeam.getHomeMatches().add(existingMatch);
            }
            if (existingMatch.getAwayTeam() != awayTeam) {
                existingMatch.getAwayTeam().getAwayMatches().remove(existingMatch);
                awayTeam.getAwayMatches().add(existingMatch);
            }

            existingMatch.setDate(convertStringToDate(matchDto.getDate()));
            existingMatch.setLeague(matchDto.getLeague());
            existingMatch.setHomeTeam(homeTeam);
            existingMatch.setAwayTeam(awayTeam);
            existingMatch.setProbability(probability);

            return matchGetMapper.toDto(matchRepository.save(existingMatch));
        } else {
            logger.error("Match with ID {} not found", id);
            throw new MatchDoesNotExistException(id);
        }
    }

    @Override
    public void deleteMatch(int id) {
        if (matchRepository.existsById(id)) {
            matchRepository.deleteById(id);
        } else {
            logger.error("Match with ID {} not found", id);
            throw new MatchDoesNotExistException(id);
        }
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
                    if (isHome) {
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
