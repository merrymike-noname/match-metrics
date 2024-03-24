package com.matchmetrics.controller;

import com.matchmetrics.entity.Match;
import com.matchmetrics.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("matchmetrics/v0/matches/api")
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

    @GetMapping("/date")
    public List<Match> getMatchesByDate(
            @RequestParam(name = "date") String date) {
        return matchService.getMatchesByDate(date);
    }

    @GetMapping("/league")
    public List<Match> getMatchesByLeague(
            @RequestParam(name = "league") String league) {
        return matchService.getMatchesByLeague(league);
    }

    @GetMapping("/team")
    public List<Match> getMatchesByTeam(
            @RequestParam(name = "name") String team) {
        return matchService.getMatchesByTeam(team);
    }

    @GetMapping("/teamSpecified")
    public List<Match> getMatchesByTeamSpecified(
            @RequestParam(name = "name") String team,
            @RequestParam(name = "isHome", required = false, defaultValue = "0") boolean isHome) {
        System.out.println(team + " " + isHome);
        return matchService.getMatchesByTeamSpecified(team, isHome);
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
