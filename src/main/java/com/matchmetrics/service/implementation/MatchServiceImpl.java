package com.matchmetrics.service.implementation;

import com.matchmetrics.entity.Match;
import com.matchmetrics.entity.Probability;
import com.matchmetrics.entity.Team;
import com.matchmetrics.entity.dto.match.MatchAddUpdateDto;
import com.matchmetrics.entity.dto.match.MatchGetDto;
import com.matchmetrics.entity.mapper.match.MatchAddUpdateMapper;
import com.matchmetrics.entity.mapper.match.MatchGetMapper;
import com.matchmetrics.entity.mapper.probability.ProbabilityGetMapper;
import com.matchmetrics.util.DateParser;
import com.matchmetrics.util.validator.ProbabilityValidator;
import com.matchmetrics.exception.*;
import com.matchmetrics.repository.MatchRepository;
import com.matchmetrics.repository.TeamRepository;
import com.matchmetrics.service.MatchService;
import com.matchmetrics.util.BindingResultInspector;
import com.matchmetrics.util.PageableCreator;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class MatchServiceImpl implements MatchService {

    private final Logger logger = LoggerFactory.getLogger(MatchServiceImpl.class);
    private final PageableCreator pageableCreator;
    private final BindingResultInspector bindingResultInspector;
    private final DateParser dateParser;
    private final ProbabilityValidator probabilityValidator;

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final MatchGetMapper matchGetMapper;
    private final MatchAddUpdateMapper matchAddUpdateMapperMapper;
    private final ProbabilityGetMapper probabilityGetMapper;

    @Autowired
    public MatchServiceImpl(PageableCreator pageableCreator,
                            BindingResultInspector bindingResultInspector,
                            DateParser dateParser,
                            ProbabilityValidator probabilityValidator,
                            MatchRepository matchRepository,
                            TeamRepository teamRepository,
                            MatchGetMapper matchGetMapper,
                            MatchAddUpdateMapper matchAddUpdateMapperMapper,
                            ProbabilityGetMapper probabilityGetMapper) {
        this.pageableCreator = pageableCreator;
        this.bindingResultInspector = bindingResultInspector;
        this.dateParser = dateParser;
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
            String homeTeam, String awayTeam, String date, String league,
            Integer page, Integer perPage, String sortBy
    ) {
        Pageable pageable = pageableCreator.createPageable(page, perPage, sortBy, Match.class);
        Specification<Match> spec = createSpecification(homeTeam, awayTeam, date, league);
        Page<Match> matches = matchRepository.findAll(spec, pageable);

        if (matches.isEmpty()) {
            logger.warn("No matches found with the given criteria. HomeTeam: {}, AwayTeam: {}, Date: {}, League: {}",
                    homeTeam, awayTeam, date, league);
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

        bindingResultInspector.checkBindingResult(bindingResult);

        dateParser.validate(matchDto.getDate());

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

            bindingResultInspector.checkBindingResult(bindingResult);
            dateParser.validate(matchDto.getDate());

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

            existingMatch.setDate(dateParser.convertStringToDate(matchDto.getDate()));
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
        Optional<Match> match = matchRepository.findById(id);
        if (match.isPresent()) {
            match.get().getHomeTeam().getHomeMatches().remove(match);
            match.get().getAwayTeam().getAwayMatches().remove(match);
            matchRepository.deleteById(id);
        } else {
            logger.error("Match with ID {} not found", id);
            throw new MatchDoesNotExistException(id);
        }

    }

    private Specification<Match> createSpecification(String homeTeam, String awayTeam, String date, String league) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            Join<Match, Team> homeTeamJoin = root.join("homeTeam", JoinType.LEFT);
            Join<Match, Team> awayTeamJoin = root.join("awayTeam", JoinType.LEFT);
            if (homeTeam != null) {
                predicates.add(criteriaBuilder.equal(homeTeamJoin.get("name"), homeTeam));
            }
            if (awayTeam != null) {
                predicates.add(criteriaBuilder.equal(awayTeamJoin.get("name"), awayTeam));
            }
            if (date != null) {
                predicates.add(criteriaBuilder.equal(root.get("date"), dateParser.convertStringToDate(date)));
            }
            if (league != null) {
                predicates.add(criteriaBuilder.equal(root.get("league"), league));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
