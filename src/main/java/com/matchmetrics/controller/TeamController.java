package com.matchmetrics.controller;

import com.matchmetrics.entity.dto.team.TeamGetDto;
import com.matchmetrics.entity.dto.team.TeamNestedDto;
import com.matchmetrics.service.TeamService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("matchmetrics/api/v0/teams")
public class TeamController {
    private final Logger logger = LoggerFactory.getLogger(TeamController.class);
    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/all")
    public List<TeamGetDto> getAllTeams(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10000") Integer perPage,
            @RequestParam(name = "sortBy", required = false, defaultValue = "default") String sortBy
    ) {
        logger.info("Received request to get all teams. Pages: {}, perPage: {}, sortBy: {}", page, perPage, sortBy);
        List<TeamGetDto> teams = teamService.getAllTeams(page - 1, perPage, sortBy);
        logger.info("Returning {} (all) teams.", teams.size());
        return teams;
    }

    @GetMapping("/{id}")
    public TeamGetDto getTeamById(@PathVariable int id) {
        logger.info("Received request to get team by ID: {}", id);
        TeamGetDto team = teamService.getTeamById(id);
        logger.info("Returning team: {}", team);
        return team;
    }

    @GetMapping()
    public List<TeamGetDto> getTeamsByCriteria(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "country", required = false) String country,
            @RequestParam(name = "elo", required = false) Float elo,
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "perPage", required = false, defaultValue = "3") Integer perPage,
            @RequestParam(name = "sortBy", required = false, defaultValue = "default") String sortBy
    ) {
        logger.info("Received request to get teams by criteria. Name: {}, country: {}, elo: {}, page: {}, perPage: {}, sortBy: {}",
                name, country, elo, page, perPage, sortBy);
        List<TeamGetDto> teams = teamService.getTeamsByCriteria(name, country, elo, page - 1, perPage, sortBy);
        logger.info("Returning {} teams by criteria", teams.size());
        return teams;
    }

    @GetMapping("/compare")
    public List<TeamGetDto> getTeamsComparedByName(@RequestParam(name = "homeTeam") String homeTeamName,
                                                   @RequestParam(name = "awayTeam") String awayTeamName
    ) {
        logger.info("Received request to compare teams: {} and {}", homeTeamName, awayTeamName);
        List<TeamGetDto> teams = teamService.getTeamsComparedByName(homeTeamName, awayTeamName);
        logger.info("Returning {} team stats", teams.size());
        return teams;
    }

    @PostMapping("/add")
    public TeamGetDto createTeam(@Valid @RequestBody TeamNestedDto team, BindingResult bindingResult) {
        logger.info("Received request to add team: {}", team);
        TeamGetDto createdTeam = teamService.createTeam(team, bindingResult);
        logger.info("Added team: {}", createdTeam);
        return createdTeam;
    }

    @PutMapping("/update/{id}")
    public TeamGetDto updateTeam(@PathVariable int id, @Valid @RequestBody TeamNestedDto team,
                                 BindingResult bindingResult) {
        logger.info("Received request to update team with ID {}: {}", id, team);
        TeamGetDto updatedTeam = teamService.updateTeam(id, team, bindingResult);
        logger.info("Updated team: {}", updatedTeam);
        return updatedTeam;
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTeam(@PathVariable int id) {
        logger.info("Received request to delete team with ID: {}", id);
        teamService.deleteTeam(id);
        logger.info("Team with ID {} deleted successfully", id);
    }
}