package com.matchmetrics.controller;

import com.matchmetrics.entity.Match;
import com.matchmetrics.entity.Probability;
import com.matchmetrics.entity.Team;
import com.matchmetrics.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/matches/api")
public class MatchController {

    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @GetMapping
    public List<Match> getAllMatches() {
        return matchService.getAllMatches();
    }

    @GetMapping("/{id}")
    public Match getMatchById(@PathVariable int id) {
        return matchService.getMatchById(id);
    }

    @GetMapping("/{date}")
    public Match getMatchesByDate(@PathVariable String date) {
        return matchService.getMatchByDate(date);
    }

    @GetMapping("/{league}")
    public Match getMatchesByLeague(@PathVariable String league) {
        return matchService.getMatchByLeague(league);
    }

    @GetMapping("/{team}")
    public Match getMatchesByTeam(@PathVariable String team) {
        return matchService.getMatchByTeam(team);
    }

    @GetMapping("/{team}/{isHome}")
    public Match getMatchesByTeamSpecified(@PathVariable String team, @PathVariable Boolean isHome ) {
        return matchService.getMatchByTeamSpecified(team, isHome);
    }

    @GetMapping("/probability/{id}")
    public Probability getProbabilityById(@PathVariable int id) {
        return matchService.getProbabilityById(id);
    }

    @GetMapping("/homeTeam/{id}")
    public Team getHomeTeamById(@PathVariable int id) {
        return matchService.getHomeTeamById(id);
    }

    @GetMapping("/awayTeam/{id}")
    public Team getAwayTeamById(@PathVariable int id) {
        return matchService.getAwayTeamById(id);
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
