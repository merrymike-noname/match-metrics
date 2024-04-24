package com.matchmetrics.service.implementation;

import com.matchmetrics.entity.dto.probability.ProbabilityGetDto;
import com.matchmetrics.service.ProbabilityService;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
public class ProbabilityServiceImpl implements ProbabilityService {
    @Override
    public List<ProbabilityGetDto> getAllProbabilities(Integer page, Integer perPage, String sortBy) {
        return List.of();
    }

    @Override
    public ProbabilityGetDto getProbabilityById(int id) {
        return null;
    }

    @Override
    public List<ProbabilityGetDto> getProbabilitiesByCriteria(
            Float probValue, Boolean higher, Integer page, Integer perPage, String sortBy) {
        return List.of();
    }

    @Override
    public ProbabilityGetDto createProbability(
            ProbabilityGetDto probability, BindingResult bindingResult) {
        return null;
    }

    @Override
    public ProbabilityGetDto updateProbability(
            int id, ProbabilityGetDto probability, BindingResult bindingResult) {
        return null;
    }

    @Override
    public void deleteProbability(int id) {

    }
}
