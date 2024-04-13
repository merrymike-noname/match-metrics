package com.matchmetrics.controller;

import com.matchmetrics.entity.dto.team.TeamGetDto;
import com.matchmetrics.entity.dto.team.TeamNestedDto;
import com.matchmetrics.service.TeamService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("matchmetrics/api/v0/teams")
public class TeamController {

    private final TeamService teamService;

    @Autowired
    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @GetMapping("/all")
    public List<TeamGetDto> getAllTeams(
            @RequestParam(name = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(name = "perPage", required = false, defaultValue = "3") Integer perPage,
            @RequestParam(name = "sortBy", required = false, defaultValue = "default") String sortBy
    ) {
        return teamService.getAllTeams(page - 1, perPage, sortBy);
    }

    @GetMapping("/{id}")
    public TeamGetDto getTeamById(@PathVariable int id) {
        return teamService.getTeamById(id);
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
        return teamService.getTeamsByCriteria(name, country, elo, page - 1, perPage, sortBy);
    }

    @PostMapping("/add")
    public TeamNestedDto createTeam(@Valid @RequestBody TeamNestedDto team, BindingResult bindingResult) {
        return teamService.createTeam(team, bindingResult);
    }

    @PutMapping("/update/{id}")
    public TeamNestedDto updateTeam(@PathVariable int id, @Valid @RequestBody TeamNestedDto team,
                                    BindingResult bindingResult) {
        return teamService.updateTeam(id, team, bindingResult);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteTeam(@PathVariable int id) {
        teamService.deleteTeam(id);
    }
}