package com.matchmetrics.service;

import com.matchmetrics.entity.Match;
import com.matchmetrics.entity.dto.main.MatchMainDto;

import java.util.List;

public interface MatchService {
    List<MatchMainDto> getAllMatches();
    MatchMainDto getMatchById(int id);
    Match addMatch(Match match);
    Match updateMatch(int id, Match match);
    void deleteMatch(int id);
    List<MatchMainDto> getMatchesByCriteria(String team, Boolean isHome, String date, String league);
}
