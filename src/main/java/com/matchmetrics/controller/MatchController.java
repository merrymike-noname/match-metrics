package com.matchmetrics.controller;

import com.matchmetrics.entity.Match;
import com.matchmetrics.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("matchmetrics/api/v0/matches")
public class MatchController {

    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping("/all")
    public List<Match> getAllMatches() {
        return matchService.getAllMatches();
    }

    @GetMapping()
    public List<Match> getMatches(
            @RequestParam(name = "team", required = false) String team,
            @RequestParam(name = "isHome", required = false) Boolean isHome,
            @RequestParam(name = "date", required = false) String date,
            @RequestParam(name = "league", required = false) String league
            ) {
        return matchService.getMatchesByCriteria(team, isHome, date, league);
    }

    @GetMapping("/{id}")
    public Match getMatchById(@PathVariable int id) {
        return matchService.getMatchById(id);
    }


    @PostMapping
    public Match addMatch(@RequestBody Match match) {
        return matchService.addMatch(match);
    }

    @PutMapping("/update/{id}")
    public Match updateMatch(@PathVariable int id, @RequestBody Match match) {
        return matchService.updateMatch(id, match);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteMatch(@PathVariable int id) {
        matchService.deleteMatch(id);
    }

}
