package com.matchmetrics.service;

import com.matchmetrics.entity.dto.match.MatchMainDto;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface MatchService {
    List<MatchMainDto> getAllMatches(Integer page, Integer perPage, String sortBy);
    List<MatchMainDto> getMatchesByCriteria(String team, Boolean isHome, String date, String league,
                                            Integer page, Integer perPage, String sortBy);
    MatchMainDto getMatchById(int id);
    MatchMainDto addMatch(MatchMainDto match, BindingResult bindingResult);
    MatchMainDto updateMatch(int id, MatchMainDto match, BindingResult bindingResult);
    void deleteMatch(int id);
}
