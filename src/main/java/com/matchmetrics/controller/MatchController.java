package com.matchmetrics.controller;

import com.matchmetrics.entity.dto.match.MatchAddUpdateDto;
import com.matchmetrics.entity.dto.match.MatchGetDto;
import com.matchmetrics.service.MatchService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("matchmetrics/api/v0/matches")
public class MatchController {
    private final Logger logger = LoggerFactory.getLogger(MatchController.class);
    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/all")
    public List<MatchGetDto> getAllMatches(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "perPage", required = false, defaultValue = "3") Integer perPage,
            @RequestParam(name = "sortBy", required = false, defaultValue = "default") String sortBy
    ) {
        logger.info("Received request to get all matches");
        List<MatchGetDto> matches = matchService.getAllMatches(page - 1, perPage, sortBy);
        logger.info("Returning {} (all) matches", matches.size());
        return matches;
    }

    @GetMapping()
    public List<MatchGetDto> getMatchesByCriteria(
            @RequestParam(name = "homeTeam", required = false) String homeTeam,
            @RequestParam(name = "awayTeam", required = false) String awayTeam,
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "league", required = false) String league,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "perPage", required = false, defaultValue = "3") Integer perPage,
            @RequestParam(name = "sortBy", required = false, defaultValue = "default") String sortBy
    ) {
        logger.info("Received request to get matches by criteria: " +
                "homeTeam={}, awayTeam={}, date={}, league={}", homeTeam, awayTeam, date, league);
        List<MatchGetDto> matches =
                matchService.getMatchesByCriteria(homeTeam, awayTeam, date, league, page - 1, perPage, sortBy);
        logger.info("Returning {} matches by criteria: " +
                "homeTeam={}, awayTeam={}, date={}, league={}", matches.size(), homeTeam, awayTeam, date, league);
        return matches;
    }

    @GetMapping("/{id}")
    public MatchGetDto getMatchById(@PathVariable int id) {
        logger.info("Received request to get match by ID: {}", id);
        MatchGetDto match = matchService.getMatchById(id);
        logger.info("Returning match: {}", match);
        return match;
    }


    @PostMapping("/add")
    public MatchGetDto addMatch(@Valid @RequestBody MatchAddUpdateDto match, BindingResult result) {
        logger.info("Received request to add match: {}", match);
        MatchGetDto addedMatch = matchService.addMatch(match, result);
        logger.info("Added match: {}", addedMatch);
        return addedMatch;
    }

    @PutMapping("/update/{id}")
    public MatchGetDto updateMatch(@PathVariable int id, @Valid @RequestBody MatchAddUpdateDto match, BindingResult result) {
        logger.info("Received request to update match with ID {}: {}", id, match);
        MatchGetDto updatedMatch = matchService.updateMatch(id, match, result);
        logger.info("Updated match: {}", updatedMatch);
        return updatedMatch;
    }

    @DeleteMapping("/delete/{id}")
    public void deleteMatch(@PathVariable int id) {
        logger.info("Received request to delete match with ID: {}", id);
        matchService.deleteMatch(id);
        logger.info("Match with ID {} deleted successfully", id);
    }

}
