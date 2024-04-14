package com.matchmetrics.team;

import com.matchmetrics.controller.TeamController;
import com.matchmetrics.controller.controller_advice.GlobalExceptionHandler;
import com.matchmetrics.entity.dto.team.TeamGetDto;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
}