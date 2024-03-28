package com.matchmetrics.controller;

import com.matchmetrics.entity.dto.match.MatchMainDto;
import com.matchmetrics.service.MatchService;
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
    public List<MatchMainDto> getAllMatches() {
        logger.info("Returning all matches...");
        return matchService.getAllMatches();
    }

    @GetMapping()
    public List<MatchMainDto> getMatchesByCriteria(
            @RequestParam(name = "team", required = false) String team,
            @RequestParam(name = "isHome", required = false) Boolean isHome,
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "league", required = false) String league
            ) {
        return matchService.getMatchesByCriteria(team, isHome, date, league);
    }

    @GetMapping("/{id}")
    public MatchMainDto getMatchById(@PathVariable int id) {
        return matchService.getMatchById(id);
    }


    @PostMapping("/add")
    public MatchMainDto addMatch(@RequestBody MatchMainDto match, BindingResult result) {
        return matchService.addMatch(match, result);
    }

    @PutMapping("/update/{id}")
    public MatchMainDto updateMatch(@PathVariable int id, @RequestBody MatchMainDto match, BindingResult result) {
        return matchService.updateMatch(id, match, result);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteMatch(@PathVariable int id) {
        matchService.deleteMatch(id);
    }

}
