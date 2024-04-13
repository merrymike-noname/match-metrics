package com.matchmetrics.service;

import com.matchmetrics.entity.dto.team.TeamGetDto;
import com.matchmetrics.entity.dto.team.TeamNestedDto;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface TeamService {
    List<TeamGetDto> getAllTeams(Integer page, Integer perPage, String sortBy);
    TeamGetDto getTeamById(int id);
    List<TeamGetDto> getTeamsByCriteria(String name, String country, Float elo,
                                  Integer page, Integer perPage, String sortBy);
    TeamNestedDto createTeam(TeamNestedDto team, BindingResult bindingResult);
    TeamNestedDto updateTeam(int id, TeamNestedDto team, BindingResult bindingResult);
    void deleteTeam(int id);
}

