package com.matchmetrics.service;

import com.matchmetrics.entity.dto.team.TeamGetDto;
import com.matchmetrics.entity.dto.team.TeamNestedDto;

import java.util.List;

public interface TeamService {
    List<TeamGetDto> getAllTeams();
    TeamGetDto getTeamById(int id);
    TeamGetDto getByCriteria(String name, String country, float elo, boolean isBiggestElo,
                             Integer page, Integer perPage, String sortBy);
    TeamNestedDto createTeam(TeamNestedDto team);
    TeamNestedDto updateTeam(int id, TeamNestedDto team);
    void deleteTeam(int id);
}

