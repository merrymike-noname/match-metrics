package com.matchmetrics.service;

import com.matchmetrics.entity.dto.team.TeamGetDto;
import com.matchmetrics.entity.dto.team.TeamNestedDto;

import java.util.List;

public interface TeamService {
    List<TeamGetDto> getAllTeams(Integer page, Integer perPage, String sortBy);
    TeamGetDto getTeamById(int id);
    List<TeamGetDto> getTeamsByCriteria(String name, String country, Float elo,
                                  Integer page, Integer perPage, String sortBy);
    TeamNestedDto createTeam(TeamNestedDto team);
    TeamNestedDto updateTeam(int id, TeamNestedDto team);
    void deleteTeam(int id);
}

