package com.matchmetrics.implementation;

import com.matchmetrics.entity.Match;
import com.matchmetrics.entity.Probability;
import com.matchmetrics.entity.Team;
import com.matchmetrics.repository.MatchRepository;
import com.matchmetrics.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;

    @Autowired
    public MatchServiceImpl(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }

    @Override
    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    @Override
    public Match getMatchById(int id) {
        Optional<Match> match = matchRepository.findById(id);
        return match.orElse(null);
    }

    @Override
    public Probability getProbabilityById(int id) {
        return null;
    }

    @Override
    public Team getAwayTeamById(int id) {
        return null;
    }

    @Override
    public Team getHomeTeamById(int id) {
        return null;
    }

    @Override
    public Match addMatch(Match match) {
        return matchRepository.save(match);
    }

    @Override
    public Match updateMatch(int id, Match match) {
        if (matchRepository.existsById(id)) {
            match.setId(id);
            return matchRepository.save(match);
        } else {
            return null;
        }
    }

    @Override
    public void deleteMatch(int id) {
        matchRepository.deleteById(id);
    }

    // TODO: Methods below

    @Override
    public Match getMatchByDate(String date) {
        return null;
    }

    @Override
    public Match getMatchByLeague(String league) {
        return null;
    }

    @Override
    public Match getMatchByTeam(String team) {
        return null;
    }

    @Override
    public Match getMatchByTeamSpecified(String team, Boolean isHome) {
        return null;
    }
}
