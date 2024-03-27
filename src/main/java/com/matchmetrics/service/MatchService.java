package com.matchmetrics.service;

import com.matchmetrics.entity.dto.match.MatchMainDto;

import java.util.List;

public interface MatchService {
    List<MatchMainDto> getAllMatches();
    MatchMainDto getMatchById(int id);
    MatchMainDto addMatch(MatchMainDto match);
    MatchMainDto updateMatch(int id, MatchMainDto match);
    void deleteMatch(int id);
    List<MatchMainDto> getMatchesByCriteria(String team, Boolean isHome, String date, String league);
}
