package com.matchmetrics.service.implementation;

import com.matchmetrics.entity.dto.team.TeamGetDto;
import com.matchmetrics.entity.dto.team.TeamNestedDto;
import com.matchmetrics.service.TeamService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamServiceImpl implements TeamService {
    @Override
    public List<TeamGetDto> getAllTeams() {
        return List.of();
    }

    @Override
    public TeamGetDto getTeamById(int id) {
        return null;
    }

    @Override
    public TeamGetDto getTeamsByCriteria(String name, String country, float elo, Integer page, Integer perPage, String sortBy) {
        return null;
    }

    @Override
    public TeamNestedDto createTeam(TeamNestedDto team) {
        return null;
    }

    @Override
    public TeamNestedDto updateTeam(int id, TeamNestedDto team) {
        return null;
    }

    @Override
    public void deleteTeam(int id) {

    }
}
