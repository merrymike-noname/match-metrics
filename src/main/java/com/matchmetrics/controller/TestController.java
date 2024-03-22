package com.matchmetrics.controller;

import com.matchmetrics.entity.Match;
import com.matchmetrics.entity.Probability;
import com.matchmetrics.entity.Team;
import com.matchmetrics.repository.MatchRepository;
import com.matchmetrics.repository.ProbabilityRepository;
import com.matchmetrics.repository.TeamRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test/api")
public class TestController {
    private final TeamRepository teamRepository;
    private final MatchRepository matchRepository;
    private final ProbabilityRepository probabilityRepository;

    public TestController(TeamRepository teamRepository, MatchRepository matchRepository, ProbabilityRepository probabilityRepository) {
        this.teamRepository = teamRepository;
        this.matchRepository = matchRepository;
        this.probabilityRepository = probabilityRepository;
    }

    @GetMapping("/teams")
    public List<Team> getAllTeams() {
        return teamRepository.findAll();
    }

    @GetMapping("/matches")
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @GetMapping("/probabilities")
    public List<Probability> getAllProbabilities() {
        return probabilityRepository.findAll();
    }
}
