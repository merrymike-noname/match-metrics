package com.matchmetrics.controller;

import com.matchmetrics.entity.dto.main.MatchMainDto;
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
    public List<MatchMainDto> getAllMatches() {
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
    public MatchMainDto addMatch(@RequestBody MatchMainDto match) {
        return matchService.addMatch(match);
    }

    @PutMapping("/update/{id}")
    public MatchMainDto updateMatch(@PathVariable int id, @RequestBody MatchMainDto match) {
        return matchService.updateMatch(id, match);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteMatch(@PathVariable int id) {
        matchService.deleteMatch(id);
    }

}
