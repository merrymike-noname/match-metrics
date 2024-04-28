package com.matchmetrics.team;

import com.matchmetrics.controller.TeamController;
import com.matchmetrics.controller.controller_advice.GlobalExceptionHandler;
import com.matchmetrics.entity.dto.team.TeamGetDto;
import com.matchmetrics.entity.dto.team.TeamNestedDto;
import com.matchmetrics.service.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class TeamControllerTest {

    @InjectMocks
    private TeamController teamController;

    @MockBean
    private TeamService teamService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        teamController = new TeamController(teamService);
        mockMvc = MockMvcBuilders.standaloneSetup(teamController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void testGetAllTeams() throws Exception {
        TeamGetDto team1 = new TeamGetDto("CWA", "USA", 1200,
                new ArrayList<>(), new ArrayList<>());

        TeamGetDto team2 = new TeamGetDto("British", "UK", 1300,
                new ArrayList<>(), new ArrayList<>());
        List<TeamGetDto> expectedTeams = Arrays.asList(team1, team2);

        when(teamService.getAllTeams(0, 3, "default")).thenReturn(expectedTeams);

        mockMvc.perform(get("/matchmetrics/api/v0/teams/all")
                        .param("page", "1")
                        .param("perPage", "3")
                        .param("sortBy", "default")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{" +
                        "\"name\":\"" + team1.getName() + "\"," +
                        "\"country\":\"" + team1.getCountry() + "\"," +
                        "\"elo\":" + team1.getElo() +
                        "},{" +
                        "\"name\":\"" + team2.getName() + "\"," +
                        "\"country\":\"" + team2.getCountry() + "\"," +
                        "\"elo\":" + team2.getElo() +
                        "}]"));

        verify(teamService, times(1)).getAllTeams(0, 3, "default");
    }

    @Test
    public void testGetTeamById() throws Exception {
        int id = 1;
        TeamGetDto expectedTeam = new TeamGetDto("CWA", "USA", 1200,
                new ArrayList<>(), new ArrayList<>());

        when(teamService.getTeamById(id)).thenReturn(expectedTeam);

        mockMvc.perform(get("/matchmetrics/api/v0/teams/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "\"name\":\"" + expectedTeam.getName() + "\"," +
                        "\"country\":\"" + expectedTeam.getCountry() + "\"," +
                        "\"elo\":" + expectedTeam.getElo() +
                        "}"));

        verify(teamService, times(1)).getTeamById(id);
    }

    @Test
    public void testGetTeamsByName() throws Exception {
        String name = "CWA";
        TeamGetDto expectedTeam = new TeamGetDto(name, "USA", 1200,
                new ArrayList<>(), new ArrayList<>());
        List<TeamGetDto> expectedTeams = Collections.singletonList(expectedTeam);

        when(teamService.getTeamsByCriteria(name, null, null, 0, 3, "default")).thenReturn(expectedTeams);

        mockMvc.perform(get("/matchmetrics/api/v0/teams")
                        .param("name", name)
                        .param("page", "1")
                        .param("perPage", "3")
                        .param("sortBy", "default")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{" +
                        "\"name\":\"" + expectedTeam.getName() + "\"," +
                        "\"country\":\"" + expectedTeam.getCountry() + "\"," +
                        "\"elo\":" + expectedTeam.getElo() +
                        "}]"));

        verify(teamService, times(1)).getTeamsByCriteria(name, null, null, 0, 3, "default");
    }

    @Test
    public void testGetTeamsByCountry() throws Exception {
        String country = "USA";
        TeamGetDto expectedTeam = new TeamGetDto("CWA", country, 1200,
                new ArrayList<>(), new ArrayList<>());
        List<TeamGetDto> expectedTeams = Collections.singletonList(expectedTeam);

        when(teamService.getTeamsByCriteria(null, country, null, 0, 3, "default")).thenReturn(expectedTeams);

        mockMvc.perform(get("/matchmetrics/api/v0/teams")
                        .param("country", country)
                        .param("page", "1")
                        .param("perPage", "3")
                        .param("sortBy", "default")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{" +
                        "\"name\":\"" + expectedTeam.getName() + "\"," +
                        "\"country\":\"" + expectedTeam.getCountry() + "\"," +
                        "\"elo\":" + expectedTeam.getElo() +
                        "}]"));

        verify(teamService, times(1)).getTeamsByCriteria(null, country, null, 0, 3, "default");
    }

    @Test
    public void testGetTeamsByElo() throws Exception {
        Float elo = 1200f;
        TeamGetDto expectedTeam = new TeamGetDto("CWA", "USA", elo,
                new ArrayList<>(), new ArrayList<>());
        List<TeamGetDto> expectedTeams = Collections.singletonList(expectedTeam);

        when(teamService.getTeamsByCriteria(null, null, elo, 0, 3, "default")).thenReturn(expectedTeams);

        mockMvc.perform(get("/matchmetrics/api/v0/teams")
                        .param("elo", elo.toString())
                        .param("page", "1")
                        .param("perPage", "3")
                        .param("sortBy", "default")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{" +
                        "\"name\":\"" + expectedTeam.getName() + "\"," +
                        "\"country\":\"" + expectedTeam.getCountry() + "\"," +
                        "\"elo\":" + expectedTeam.getElo() +
                        "}]"));

        verify(teamService, times(1)).getTeamsByCriteria(null, null, elo, 0, 3, "default");
    }

    @Test
    public void testGetTeamsComparedByName() throws Exception {
        String teamHome = "Liverpool";
        String teamAway = "Arsenal";
        TeamGetDto teamHomeDto = new TeamGetDto(teamHome, "England", 2000, new ArrayList<>(), new ArrayList<>());
        TeamGetDto teamAwayDto = new TeamGetDto(teamAway, "England", 1900, new ArrayList<>(), new ArrayList<>());

        when(teamService.getTeamsComparedByName(teamHome, teamAway)).thenReturn(List.of(teamHomeDto, teamAwayDto));

        mockMvc.perform(get("/matchmetrics/api/v0/teams/compare")
                        .param("homeTeam", teamHome)
                        .param("awayTeam", teamAway)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{" +
                        "\"name\":\"" + teamHomeDto.getName() + "\"," +
                        "\"country\":\"" + teamHomeDto.getCountry() + "\"," +
                        "\"elo\":" + teamHomeDto.getElo() +
                        "},{" +
                        "\"name\":\"" + teamAwayDto.getName() + "\"," +
                        "\"country\":\"" + teamAwayDto.getCountry() + "\"," +
                        "\"elo\":" + teamAwayDto.getElo() +
                        "}]"));

        verify(teamService, times(1)).getTeamsComparedByName(teamHome, teamAway);
    }

    @Test
    public void testCreateTeam() throws Exception {
        TeamNestedDto team = new TeamNestedDto("CWA", "USA", 1200);
        TeamGetDto teamGetDto = new TeamGetDto("CWA", "USA", 1200, new ArrayList<>(), new ArrayList<>());

        when(teamService.createTeam(any(TeamNestedDto.class), any(BindingResult.class))).thenReturn(teamGetDto);

        mockMvc.perform(post("/matchmetrics/api/v0/teams/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"CWA\",\"country\":\"USA\",\"elo\":1200}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "\"name\":\"" + team.getName() + "\"," +
                        "\"country\":\"" + team.getCountry() + "\"," +
                        "\"elo\":" + team.getElo() +
                        "}"));

        verify(teamService, times(1)).createTeam(any(TeamNestedDto.class), any(BindingResult.class));
    }

    @Test
    public void testUpdateTeam() throws Exception {
        int id = 1;
        TeamNestedDto team = new TeamNestedDto("CWA", "USA", 1300);
        TeamGetDto teamGetDto = new TeamGetDto("CWA", "USA", 1300, new ArrayList<>(), new ArrayList<>());

        when(teamService.updateTeam(eq(id), any(TeamNestedDto.class), any(BindingResult.class))).thenReturn(teamGetDto);

        mockMvc.perform(put("/matchmetrics/api/v0/teams/update/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"CWA\",\"country\":\"USA\",\"elo\":1300}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{" +
                        "\"name\":\"" + team.getName() + "\"," +
                        "\"country\":\"" + team.getCountry() + "\"," +
                        "\"elo\":" + team.getElo() +
                        "}"));

        verify(teamService, times(1)).updateTeam(eq(id), any(TeamNestedDto.class), any(BindingResult.class));
    }

    @Test
    public void testDeleteTeam() throws Exception {
        int id = 1;

        doNothing().when(teamService).deleteTeam(id);

        mockMvc.perform(delete("/matchmetrics/api/v0/teams/delete/" + id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(teamService, times(1)).deleteTeam(id);
    }

}