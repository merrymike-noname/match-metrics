package com.matchmetrics.service.implementation;

import com.matchmetrics.entity.Probability;
import com.matchmetrics.entity.dto.probability.ProbabilityGetDto;
import com.matchmetrics.entity.mapper.probability.ProbabilityGetMapper;
import com.matchmetrics.exception.AssociatedProbabilityException;
import com.matchmetrics.exception.ProbabilityDoesNotExistException;
import com.matchmetrics.repository.MatchRepository;
import com.matchmetrics.repository.ProbabilityRepository;
import com.matchmetrics.service.ProbabilityService;
import com.matchmetrics.util.PageableCreator;
import jakarta.persistence.criteria.Predicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProbabilityServiceImpl implements ProbabilityService {

    private final Logger logger = LoggerFactory.getLogger(ProbabilityServiceImpl.class);
    private final PageableCreator pageableCreator;

    private final ProbabilityRepository probabilityRepository;
    private final MatchRepository matchRepository;
    private final ProbabilityGetMapper probabilityGetMapper;

    public ProbabilityServiceImpl(ProbabilityRepository probabilityRepository,
                                  ProbabilityGetMapper probabilityGetMapper,
                                  PageableCreator pageableCreator,
                                  MatchRepository matchRepository) {
        this.probabilityRepository = probabilityRepository;
        this.probabilityGetMapper = probabilityGetMapper;
        this.pageableCreator = pageableCreator;
        this.matchRepository = matchRepository;
    }

    @Override
    public List<ProbabilityGetDto> getAllProbabilities(Integer page, Integer perPage, String sortBy) {
        Pageable pageable = pageableCreator.createPageable(page, perPage, sortBy, Probability.class);

        return probabilityRepository.findAll(pageable).stream()
                .map(probabilityGetMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ProbabilityGetDto getProbabilityById(int id) {
        Optional<Probability> probability = probabilityRepository.findById(id);
        if (probability.isEmpty()) {
            logger.error("Probability with ID {} not found", id);
            throw new ProbabilityDoesNotExistException(id);
        }
        return probabilityGetMapper.toDto(probability.get());
    }

    @Override
    public List<ProbabilityGetDto> getProbabilitiesByCriteria(
            Float probValue, Boolean higher, Integer page, Integer perPage, String sortBy) {
        Pageable pageable = pageableCreator.createPageable(page, perPage, sortBy, Probability.class);
        Specification<Probability> spec = createProbabilitySpecification(probValue, higher);
        Page<Probability> probabilities = probabilityRepository.findAll(spec, pageable);

        if (probabilities.isEmpty()) {
            logger.warn("No probabilities found with the given criteria. Probability Value: {}, Higher: {}", probValue, higher);
        }

        return probabilities.getContent().stream()
                .map(probabilityGetMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public ProbabilityGetDto createProbability(
            ProbabilityGetDto probabilityDto, BindingResult bindingResult) {
        Probability probability = probabilityGetMapper.toEntity(probabilityDto);
        Probability savedProbability = probabilityRepository.save(probability);
        return probabilityGetMapper.toDto(savedProbability);
    }

    @Override
    public ProbabilityGetDto updateProbability(
            int id, ProbabilityGetDto probabilityDto, BindingResult bindingResult) {
        Optional<Probability> optionalProbability = probabilityRepository.findById(id);
        if (optionalProbability.isEmpty()) {
            logger.error("Probability with ID {} not found", id);
            throw new ProbabilityDoesNotExistException(id);
        }
        Probability probability = optionalProbability.get();
        probability.setHomeTeamWin(probabilityDto.getHomeTeamWin());
        probability.setDraw(probabilityDto.getDraw());
        probability.setAwayTeamWin(probabilityDto.getAwayTeamWin());
        Probability updatedProbability = probabilityRepository.save(probability);
        return probabilityGetMapper.toDto(updatedProbability);
    }

    @Override
    public void deleteProbability(int id) {
        if (!probabilityRepository.existsById(id)) {
            logger.error("Probability with ID {} not found", id);
            throw new ProbabilityDoesNotExistException(id);
        }
        if (matchRepository.existsByProbabilityId(id)) {
            logger.error("Probability with ID {} is associated with a match and cannot be deleted", id);
            throw new AssociatedProbabilityException(id);
        }
        probabilityRepository.deleteById(id);
    }

    private Specification<Probability> createProbabilitySpecification(Float probValue, Boolean higher) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (probValue != null) {
                if (higher != null && higher) {
                    predicates.add(
                            criteriaBuilder.or(
                                    criteriaBuilder.greaterThanOrEqualTo(root.get("homeTeamWin"), probValue),
                                    criteriaBuilder.greaterThanOrEqualTo(root.get("draw"), probValue),
                                    criteriaBuilder.greaterThanOrEqualTo(root.get("awayTeamWin"), probValue)
                            )
                    );

                } else {
                    predicates.add(
                            criteriaBuilder.or(
                                    criteriaBuilder.lessThanOrEqualTo(root.get("homeTeamWin"), probValue),
                                    criteriaBuilder.lessThanOrEqualTo(root.get("draw"), probValue),
                                    criteriaBuilder.lessThanOrEqualTo(root.get("awayTeamWin"), probValue)
                            )
                    );
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

}
