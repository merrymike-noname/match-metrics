package com.matchmetrics.service;

import com.matchmetrics.entity.Match;

import java.util.List;

public interface MatchService {
    List<Match> getAllMatches();
    Match getMatchById(int id);
    Match addMatch(Match match);
    Match updateMatch(int id, Match match);
    void deleteMatch(int id);
    List<Match> getMatchesByDate(String date);
    List<Match> getMatchesByLeague(String league);
    List<Match> getMatchesByTeam(String team);
    List<Match> getMatchesByTeamSpecified(String team, boolean isHome);
}
