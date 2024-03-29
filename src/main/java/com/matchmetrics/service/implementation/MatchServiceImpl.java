package com.matchmetrics.service.implementation;

import com.matchmetrics.entity.Match;
import com.matchmetrics.entity.Team;
import com.matchmetrics.entity.dto.match.MatchMainDto;
import com.matchmetrics.entity.mapper.match.MatchMainMapper;
import com.matchmetrics.exception.DateConversionException;
import com.matchmetrics.exception.FieldDoesNotExistException;
import com.matchmetrics.exception.MatchDoesNotExistException;
import com.matchmetrics.exception.InvalidDataException;
import com.matchmetrics.repository.MatchRepository;
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
    private final MatchMainMapper matchMainMapper;

    @Autowired
    public MatchServiceImpl(MatchRepository matchRepository, MatchMainMapper matchMainMapper) {
        this.matchRepository = matchRepository;
        this.matchMainMapper = matchMainMapper;
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
    public MatchMainDto addMatch(MatchMainDto match, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            StringBuilder errorMessage = new StringBuilder();
            bindingResult.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append("\n"));
            logger.error("Error occurred while adding match: {}", errorMessage);
            throw new InvalidDataException(errorMessage.toString());
        }
        return matchMainMapper.toDto(matchRepository.save(matchMainMapper.toEntity(match)));
    }

    @Override
    public MatchMainDto updateMatch(int id, MatchMainDto match, BindingResult bindingResult) {
        if (matchRepository.existsById(id)) {
            if (bindingResult.hasErrors()) {
                StringBuilder errorMessage = new StringBuilder();
                bindingResult.getAllErrors().forEach(error -> errorMessage.append(error.getDefaultMessage()).append("\n"));
                logger.error("Error occurred while updating match: {}", errorMessage);
                throw new InvalidDataException(errorMessage.toString());
            }
            Match matchEntity = matchMainMapper.toEntity(match);
            matchEntity.setId(id);
            return matchMainMapper.toDto(matchRepository.save(matchEntity));
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
