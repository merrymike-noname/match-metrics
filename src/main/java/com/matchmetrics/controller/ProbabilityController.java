package com.matchmetrics.controller;

import com.matchmetrics.entity.dto.probability.ProbabilityGetDto;
import com.matchmetrics.service.ProbabilityService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("matchmetrics/api/v0/probabilities")
public class ProbabilityController {
    private final Logger logger = LoggerFactory.getLogger(ProbabilityController.class);
    private final ProbabilityService probabilityService;

    public ProbabilityController(ProbabilityService probabilityService) {
        this.probabilityService = probabilityService;
    }

    @GetMapping("/all")
    public List<ProbabilityGetDto> getAllProbabilities(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "perPage", required = false, defaultValue = "3") Integer perPage,
            @RequestParam(name = "sortBy", required = false, defaultValue = "default") String sortBy
    ) {
        logger.info("Received request to get all probabilities. Pages: {}, perPage: {}, sortBy: {}", page, perPage, sortBy);
        List<ProbabilityGetDto> probabilities = probabilityService.getAllProbabilities(page - 1, perPage, sortBy);
        logger.info("Returning {} (all) probabilities.", probabilities.size());
        return probabilities;
    }

    @GetMapping("/{id}")
    public ProbabilityGetDto getProbabilityById(@PathVariable int id) {
        logger.info("Received request to get probability by ID: {}", id);
        ProbabilityGetDto probability = probabilityService.getProbabilityById(id);
        logger.info("Returning probability: {}", probability);
        return probability;
    }

    @GetMapping()
    public List<ProbabilityGetDto> getProbabilitiesByCriteria(
            @RequestParam(name = "probability", required = false) Float probability,
            @RequestParam(name = "higher", required = false, defaultValue = "true") Boolean higher,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "perPage", required = false, defaultValue = "3") Integer perPage,
            @RequestParam(name = "sortBy", required = false, defaultValue = "default") String sortBy
    ) {
        logger.info("Received request to get probabilities by criteria. Value: {}, isHigher: {}, page: {}, perPage: {}, sortBy: {}",
                probability, higher, page, perPage, sortBy);
        List<ProbabilityGetDto> probabilities =
                probabilityService.getProbabilitiesByCriteria(probability, higher, page - 1, perPage, sortBy);
        logger.info("Returning {} probabilities by criteria", probabilities.size());
        return probabilities;
    }

    @PostMapping("/add")
    public ProbabilityGetDto createProbability(@Valid @RequestBody ProbabilityGetDto probability, BindingResult bindingResult) {
        logger.info("Received request to add probability: {}", probability);
        ProbabilityGetDto createdProbability = probabilityService.createProbability(probability, bindingResult);
        logger.info("Added probability: {}", createdProbability);
        return createdProbability;
    }

    @PutMapping("/update/{id}")
    public ProbabilityGetDto updateProbability(@PathVariable int id, @Valid @RequestBody ProbabilityGetDto probability,
                                               BindingResult bindingResult) {
        logger.info("Received request to update probability with ID {}: {}", id, probability);
        ProbabilityGetDto updatedProbability = probabilityService.updateProbability(id, probability, bindingResult);
        logger.info("Updated probability: {}", updatedProbability);
        return updatedProbability;
    }

    @DeleteMapping("/delete/{id}")
    public void deleteProbability(@PathVariable int id) {
        logger.info("Received request to delete probability with ID: {}", id);
        probabilityService.deleteProbability(id);
        logger.info("Probability with ID {} deleted successfully", id);
    }
}
