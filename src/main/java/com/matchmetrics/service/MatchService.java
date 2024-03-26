package com.matchmetrics.service;

import com.matchmetrics.entity.Match;

import java.util.List;

public interface MatchService {
    List<Match> getAllMatches();
    Match getMatchById(int id);
    Match addMatch(Match match);
    Match updateMatch(int id, Match match);
    void deleteMatch(int id);
    List<Match> getMatchesByCriteria(String team, Boolean isHome, String date, String league);
}
