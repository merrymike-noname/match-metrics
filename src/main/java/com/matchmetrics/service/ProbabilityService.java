package com.matchmetrics.service;

import com.matchmetrics.entity.dto.probability.ProbabilityGetDto;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface ProbabilityService {
    List<ProbabilityGetDto> getAllProbabilities(Integer page, Integer perPage, String sortBy);
    ProbabilityGetDto getProbabilityById(int id);
    List<ProbabilityGetDto> getProbabilitiesByCriteria(
            Float probValue, Boolean higher, Integer page, Integer perPage, String sortBy);
    ProbabilityGetDto createProbability(ProbabilityGetDto probability, BindingResult bindingResult);
    ProbabilityGetDto updateProbability(int id, ProbabilityGetDto probability, BindingResult bindingResult);
    void deleteProbability(int id);
}
