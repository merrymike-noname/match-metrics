package com.matchmetrics.controller;

import com.matchmetrics.entity.dto.match.MatchAddUpdateDto;
import com.matchmetrics.entity.dto.match.MatchMainDto;
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
    public List<MatchMainDto> getAllMatches(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "perPage", required = false, defaultValue = "3") Integer perPage,
            @RequestParam(name = "sortBy", required = false, defaultValue = "default") String sortBy
    ) {
        logger.info("Received request to get all matches");
        List<MatchMainDto> matches = matchService.getAllMatches(page - 1, perPage, sortBy);
        logger.info("Returning {} (all) matches", matches.size());
        return matches;
    }

    @GetMapping()
    public List<MatchMainDto> getMatchesByCriteria(
            @RequestParam(name = "team", required = false) String team,
            @RequestParam(name = "isHome", required = false) Boolean isHome,
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "league", required = false) String league,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "perPage", required = false, defaultValue = "3") Integer perPage,
            @RequestParam(name = "sortBy", required = false, defaultValue = "default") String sortBy
    ) {
        logger.info("Received request to get matches by criteria: " +
                "team={}, isHome={}, date={}, league={}", team, isHome, date, league);
        List<MatchMainDto> matches =
                matchService.getMatchesByCriteria(team, isHome, date, league, page - 1, perPage, sortBy);
        logger.info("Returning {} matches by criteria: " +
                "team={}, isHome={}, date={}, league={}", matches.size(), team, isHome, date, league);
        return matches;
    }

    @GetMapping("/{id}")
    public MatchMainDto getMatchById(@PathVariable int id) {
        logger.info("Received request to get match by ID: {}", id);
        MatchMainDto match = matchService.getMatchById(id);
        logger.info("Returning match: {}", match);
        return match;
    }


    @PostMapping("/add")
    public MatchMainDto addMatch(@Valid @RequestBody MatchAddUpdateDto match, BindingResult result) {
        logger.info("Received request to add match: {}", match);
        MatchMainDto addedMatch = matchService.addMatch(match, result);
        logger.info("Added match: {}", addedMatch);
        return addedMatch;
    }

    @PutMapping("/update/{id}")
    public MatchMainDto updateMatch(@PathVariable int id, @Valid @RequestBody MatchAddUpdateDto match, BindingResult result) {
        logger.info("Received request to update match with ID {}: {}", id, match);
        MatchMainDto updatedMatch = matchService.updateMatch(id, match, result);
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
