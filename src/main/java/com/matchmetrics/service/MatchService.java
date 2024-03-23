package com.matchmetrics.service;

import com.matchmetrics.entity.Match;
import com.matchmetrics.entity.Probability;
import com.matchmetrics.entity.Team;

import java.util.List;

public interface MatchService {
    List<Match> getAllMatches();
    Match getMatchById(int id);
    Probability getProbabilityById(int id);
    Team getAwayTeamById(int id);
    Team getHomeTeamById(int id);
    Match addMatch(Match match);
    Match updateMatch(int id, Match match);
    void deleteMatch(int id);
    Match getMatchByDate(String date);
    Match getMatchByLeague(String league);
    Match getMatchByTeam(String team);
    Match getMatchByTeamSpecified(String team, Boolean isHome);
}
