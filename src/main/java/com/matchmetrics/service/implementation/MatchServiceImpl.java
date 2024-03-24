package com.matchmetrics.service.implementation;

import com.matchmetrics.entity.Match;
import com.matchmetrics.exceptions.MatchUpdateException;
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
    public Match addMatch(Match match) {
        return matchRepository.save(match);
    }

    @Override
    public Match updateMatch(int id, Match match) {
        if (matchRepository.existsById(id)) {
            match.setId(id);
            return matchRepository.save(match);
        } else {
            throw new MatchUpdateException(id);
        }
    }

    @Override
    public void deleteMatch(int id) {
        matchRepository.deleteById(id);
    }

    // TODO: Methods below

    @Override
    public List<Match> getMatchesByDate(String date) {
        return null;
    }

    @Override
    public List<Match> getMatchesByLeague(String league) {
        return null;
    }

    @Override
    public List<Match> getMatchesByTeam(String team) {
        return matchRepository.findMatchesByTeam(team);
    }

    @Override
    public List<Match> getMatchesByTeamSpecified(String team, boolean isHome) {
        if (isHome) {
            return matchRepository.findHomeMatchesByTeam(team);
        } else {
            return matchRepository.findAwayMatchesByTeam(team);
        }
    }
}
