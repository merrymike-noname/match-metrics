package com.matchmetrics.service;

import com.matchmetrics.entity.dto.match.MatchAddUpdateDto;
import com.matchmetrics.entity.dto.match.MatchGetDto;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface MatchService {
    List<MatchGetDto> getAllMatches(Integer page, Integer perPage, String sortBy);
    List<MatchGetDto> getMatchesByCriteria(String team, Boolean isHome, String date, String league,
                                           Integer page, Integer perPage, String sortBy);
    MatchGetDto getMatchByTeams(String homeTeam, String awayTeam);
    MatchGetDto getMatchById(int id);
    MatchGetDto addMatch(MatchAddUpdateDto match, BindingResult bindingResult);
    MatchGetDto updateMatch(int id, MatchAddUpdateDto match, BindingResult bindingResult);
    void deleteMatch(int id);
}
