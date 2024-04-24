package com.matchmetrics.service.implementation;

import com.matchmetrics.entity.Probability;
import com.matchmetrics.entity.dto.probability.ProbabilityGetDto;
import com.matchmetrics.exception.FieldDoesNotExistException;
import com.matchmetrics.exception.ProbabilityDoesNotExistException;
import com.matchmetrics.service.ProbabilityService;
import com.matchmetrics.repository.ProbabilityRepository;
import com.matchmetrics.entity.mapper.probability.ProbabilityGetMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;
import jakarta.persistence.criteria.Predicate;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;
import java.util.ArrayList;

@Service
public class ProbabilityServiceImpl implements ProbabilityService {

    private final Logger logger = LoggerFactory.getLogger(ProbabilityServiceImpl.class);

    private final ProbabilityRepository probabilityRepository;
    private final ProbabilityGetMapper probabilityGetMapper;

    public ProbabilityServiceImpl(ProbabilityRepository probabilityRepository,
                                  ProbabilityGetMapper probabilityGetMapper) {
        this.probabilityRepository = probabilityRepository;
        this.probabilityGetMapper = probabilityGetMapper;
    }

    @Override
    public List<ProbabilityGetDto> getAllProbabilities(Integer page, Integer perPage, String sortBy) {
        Pageable pageable = createProbabilityPageable(page, perPage, sortBy);

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
        Pageable pageable = createProbabilityPageable(page, perPage, sortBy);
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
        probability.setAwayTeamWin(probabilityDto.getHomeTeamWin(), probabilityDto.getDraw());
        Probability updatedProbability = probabilityRepository.save(probability);
        return probabilityGetMapper.toDto(updatedProbability);
    }

    @Override
    public void deleteProbability(int id) {
        if (!probabilityRepository.existsById(id)) {
            logger.error("Probability with ID {} not found", id);
            throw new ProbabilityDoesNotExistException(id);
        }
        probabilityRepository.deleteById(id);
    }

    private Pageable createProbabilityPageable(int page, int perPage, String sortBy) {
        if (!sortBy.equals("default") && !checkIfProbabilityFieldExists(sortBy)) {
            logger.error("Error occurred while trying to find a '{}' field in {}", sortBy, Probability.class.getName());
            throw new FieldDoesNotExistException(sortBy, Probability.class);
        }
        Sort sort = sortBy.equals("default") ? Sort.unsorted() : Sort.by(sortBy);
        return PageRequest.of(page, perPage, sort);
    }

    private boolean checkIfProbabilityFieldExists(String fieldName) {
        return Arrays.stream(Probability.class.getDeclaredFields())
                .anyMatch(field -> field.getName().equals(fieldName));
    }

    private Specification<Probability> createProbabilitySpecification(Float probValue, Boolean higher) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (probValue != null) {
                if (higher != null && higher) {
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("probValue"), probValue));
                } else {
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("probValue"), probValue));
                }
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }

}
