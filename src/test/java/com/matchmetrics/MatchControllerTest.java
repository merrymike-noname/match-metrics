package com.matchmetrics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.matchmetrics.controller.MatchController;
import com.matchmetrics.controller.controller_advice.GlobalExceptionHandler;
import com.matchmetrics.entity.dto.match.MatchAddUpdateDto;
import com.matchmetrics.entity.dto.match.MatchMainDto;
import com.matchmetrics.entity.dto.probability.ProbabilityMainDto;
import com.matchmetrics.entity.dto.team.TeamNameDto;
import com.matchmetrics.entity.dto.team.TeamNestedDto;
import com.matchmetrics.exception.MatchDoesNotExistException;
import com.matchmetrics.service.MatchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(MockitoExtension.class)
public class MatchControllerTest {

    @Mock
    private MatchService matchService;

    @InjectMocks
    private MatchController matchController;

    private MockMvc mockMvc;


    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(matchController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }


    @Test
    public void testGetAllMatches() throws Exception {
        TeamNestedDto homeTeam = new TeamNestedDto("Test1", "LeagueA", 1500);
        TeamNestedDto awayTeam = new TeamNestedDto("Test2", "LeagueB", 1400);
        ProbabilityMainDto probability = new ProbabilityMainDto(0.4f, 0.3f, 0.3f);
        MatchMainDto match1 = new MatchMainDto(new Date(), "LeagueA", homeTeam, awayTeam, probability);
        MatchMainDto match2 = new MatchMainDto(new Date(), "LeagueB", homeTeam, awayTeam, probability);

        List<MatchMainDto> matches = Arrays.asList(match1, match2);

        when(matchService.getAllMatches(0, 3, "default")).thenReturn(matches);

        mockMvc.perform(get("/matchmetrics/api/v0/matches/all")
                        .param("page", "1")
                        .param("perPage", "3")
                        .param("sortBy", "default")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{" +
                        "\"date\":" + match1.getDate().getTime() + "," +
                        "\"league\":\"" + match1.getLeague() + "\"," +
                        "\"homeTeam\":{" +
                        "\"name\":\"" + match1.getHomeTeam().getName() + "\"," +
                        "\"country\":\"" + match1.getHomeTeam().getCountry() + "\"," +
                        "\"elo\":" + match1.getHomeTeam().getElo() +
                        "}," +
                        "\"awayTeam\":{" +
                        "\"name\":\"" + match1.getAwayTeam().getName() + "\"," +
                        "\"country\":\"" + match1.getAwayTeam().getCountry() + "\"," +
                        "\"elo\":" + match1.getAwayTeam().getElo() +
                        "}," +
                        "\"probability\":{" +
                        "\"homeTeamWin\":" + match1.getProbability().getHomeTeamWin() + "," +
                        "\"draw\":" + match1.getProbability().getDraw() + "," +
                        "\"awayTeamWin\":" + match1.getProbability().getAwayTeamWin() +
                        "}" +
                        "},{" +
                        "\"date\":" + match2.getDate().getTime() + "," +
                        "\"league\":\"" + match2.getLeague() + "\"," +
                        "\"homeTeam\":{" +
                        "\"name\":\"" + match2.getHomeTeam().getName() + "\"," +
                        "\"country\":\"" + match2.getHomeTeam().getCountry() + "\"," +
                        "\"elo\":" + match2.getHomeTeam().getElo() +
                        "}," +
                        "\"awayTeam\":{" +
                        "\"name\":\"" + match2.getAwayTeam().getName() + "\"," +
                        "\"country\":\"" + match2.getAwayTeam().getCountry() + "\"," +
                        "\"elo\":" + match2.getAwayTeam().getElo() +
                        "}," +
                        "\"probability\":{" +
                        "\"homeTeamWin\":" + match2.getProbability().getHomeTeamWin() + "," +
                        "\"draw\":" + match2.getProbability().getDraw() + "," +
                        "\"awayTeamWin\":" + match2.getProbability().getAwayTeamWin() +
                        "}" +
                        "}]"));
    }

    @Test
    public void testDeleteMatch() throws Exception {
        int id = 1;

        doNothing().when(matchService).deleteMatch(id);

        mockMvc.perform(delete("/matchmetrics/api/v0/matches/delete/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(matchService, times(1)).deleteMatch(id);
    }

    @Test
    public void testGetMatchesByCriteria_isHome() throws Exception {
        String team = "Test1";
        Boolean isHome = true;
        String date = "2022-01-01";
        String league = "LeagueA";
        int page = 1;
        int perPage = 3;
        String sortBy = "default";

        TeamNestedDto homeTeam = new TeamNestedDto("Test1", "LeagueA", 1500);
        TeamNestedDto awayTeam = new TeamNestedDto("Test2", "LeagueB", 1400);
        ProbabilityMainDto probability = new ProbabilityMainDto(0.4f, 0.3f, 0.3f);
        MatchMainDto match1 = new MatchMainDto(new SimpleDateFormat("yyyy-MM-dd").parse(date), "LeagueA", homeTeam, awayTeam, probability);
        MatchMainDto match2 = new MatchMainDto(new SimpleDateFormat("yyyy-MM-dd").parse(date), "LeagueA", awayTeam, homeTeam, probability);

        when(matchService.getMatchesByCriteria(eq(team), eq(isHome), any(), eq(league), eq(page - 1), eq(perPage), eq(sortBy)))
                .thenReturn(List.of(match1));

        mockMvc.perform(get("/matchmetrics/api/v0/matches")
                        .param("team", team)
                        .param("isHome", isHome.toString())
                        .param("league", league)
                        .param("page", Integer.toString(page))
                        .param("perPage", Integer.toString(perPage))
                        .param("sortBy", sortBy)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{" +
                        "\"date\":" + match1.getDate().getTime() + "," +
                        "\"league\":\"" + match1.getLeague() + "\"," +
                        "\"homeTeam\":{" +
                        "\"name\":\"" + match1.getHomeTeam().getName() + "\"," +
                        "\"country\":\"" + match1.getHomeTeam().getCountry() + "\"," +
                        "\"elo\":" + match1.getHomeTeam().getElo() +
                        "}," +
                        "\"awayTeam\":{" +
                        "\"name\":\"" + match1.getAwayTeam().getName() + "\"," +
                        "\"country\":\"" + match1.getAwayTeam().getCountry() + "\"," +
                        "\"elo\":" + match1.getAwayTeam().getElo() +
                        "}," +
                        "\"probability\":{" +
                        "\"homeTeamWin\":" + match1.getProbability().getHomeTeamWin() + "," +
                        "\"draw\":" + match1.getProbability().getDraw() + "," +
                        "\"awayTeamWin\":" + match1.getProbability().getAwayTeamWin() +
                        "}" +
                        "}]"));
    }

    @Test
    public void testGetMatchesByCriteria_date() throws Exception {
        String date1 = "2022-01-01";
        int page = 1;
        int perPage = 3;
        String sortBy = "default";

        TeamNestedDto homeTeam = new TeamNestedDto("Test1", "LeagueA", 1500);
        TeamNestedDto awayTeam = new TeamNestedDto("Test2", "LeagueB", 1400);
        ProbabilityMainDto probability = new ProbabilityMainDto(0.4f, 0.3f, 0.3f);
        MatchMainDto match1 = new MatchMainDto(new SimpleDateFormat("yyyy-MM-dd").parse(date1), "LeagueA", homeTeam, awayTeam, probability);

        when(matchService.getMatchesByCriteria(any(), any(), eq(date1), any(), anyInt(), anyInt(), anyString())).thenReturn(List.of(match1));

        mockMvc.perform(get("/matchmetrics/api/v0/matches")
                        .param("date", date1)
                        .param("page", Integer.toString(page))
                        .param("perPage", Integer.toString(perPage))
                        .param("sortBy", sortBy)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{" +
                        "\"date\":" + match1.getDate().getTime() + "," +
                        "\"league\":\"" + match1.getLeague() + "\"," +
                        "\"homeTeam\":{" +
                        "\"name\":\"" + match1.getHomeTeam().getName() + "\"," +
                        "\"country\":\"" + match1.getHomeTeam().getCountry() + "\"," +
                        "\"elo\":" + match1.getHomeTeam().getElo() +
                        "}," +
                        "\"awayTeam\":{" +
                        "\"name\":\"" + match1.getAwayTeam().getName() + "\"," +
                        "\"country\":\"" + match1.getAwayTeam().getCountry() + "\"," +
                        "\"elo\":" + match1.getAwayTeam().getElo() +
                        "}," +
                        "\"probability\":{" +
                        "\"homeTeamWin\":" + match1.getProbability().getHomeTeamWin() + "," +
                        "\"draw\":" + match1.getProbability().getDraw() + "," +
                        "\"awayTeamWin\":" + match1.getProbability().getAwayTeamWin() +
                        "}" +
                        "}]"));
    }

    @Test
    public void testGetMatchesByCriteria_league() throws Exception {
        String date2 = "2022-01-02";
        int page = 1;
        int perPage = 3;
        String sortBy = "default";
        String league = "LeagueBB";

        TeamNestedDto homeTeam = new TeamNestedDto("Test1", "LeagueA", 1500);
        TeamNestedDto newTeam = new TeamNestedDto("Test3", "LeagueC", 1300);
        ProbabilityMainDto probabilitySecond = new ProbabilityMainDto(0.5f, 0.25f, 0.025f);
        MatchMainDto match2 = new MatchMainDto(new SimpleDateFormat("yyyy-MM-dd").parse(date2), "LeagueBB", homeTeam, newTeam, probabilitySecond);

        when(matchService.getMatchesByCriteria(any(), any(), any(), eq(league), anyInt(), anyInt(), anyString())).thenReturn(List.of(match2));

        mockMvc.perform(get("/matchmetrics/api/v0/matches")
                        .param("league", league)
                        .param("page", Integer.toString(page))
                        .param("perPage", Integer.toString(perPage))
                        .param("sortBy", sortBy)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{" +
                        "\"date\":" + match2.getDate().getTime() + "," +
                        "\"league\":\"" + match2.getLeague() + "\"," +
                        "\"homeTeam\":{" +
                        "\"name\":\"" + match2.getHomeTeam().getName() + "\"," +
                        "\"country\":\"" + match2.getHomeTeam().getCountry() + "\"," +
                        "\"elo\":" + match2.getHomeTeam().getElo() +
                        "}," +
                        "\"awayTeam\":{" +
                        "\"name\":\"" + match2.getAwayTeam().getName() + "\"," +
                        "\"country\":\"" + match2.getAwayTeam().getCountry() + "\"," +
                        "\"elo\":" + match2.getAwayTeam().getElo() +
                        "}," +
                        "\"probability\":{" +
                        "\"homeTeamWin\":" + match2.getProbability().getHomeTeamWin() + "," +
                        "\"draw\":" + match2.getProbability().getDraw() + "," +
                        "\"awayTeamWin\":" + match2.getProbability().getAwayTeamWin() +
                        "}" +
                        "}]"));
    }

    @Test
    public void testGetMatchesByCriteria_teamAndLeague() throws Exception {
        String team = "Test1";
        String league = "LeagueA";
        int page = 1;
        int perPage = 3;
        String sortBy = "default";

        when(matchService.getMatchesByCriteria(eq(team), any(), any(), eq(league), anyInt(), anyInt(), anyString()))
                .thenReturn(List.of(new MatchMainDto()));

        mockMvc.perform(get("/matchmetrics/api/v0/matches")
                        .param("team", team)
                        .param("league", league)
                        .param("page", Integer.toString(page))
                        .param("perPage", Integer.toString(perPage))
                        .param("sortBy", sortBy)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateMatch() throws Exception {
        int id = 1;
        MatchAddUpdateDto matchDto = new MatchAddUpdateDto();
        matchDto.setDate("2024-10-10");
        matchDto.setLeague("LeagueA");
        matchDto.setHomeTeam(new TeamNameDto("Test1"));
        matchDto.setAwayTeam(new TeamNameDto("Test2"));
        matchDto.setProbability(new ProbabilityMainDto(0.4f, 0.3f, 0.3f));

        MatchMainDto updatedMatch = new MatchMainDto();
        updatedMatch.setDate(new Date());
        updatedMatch.setLeague(matchDto.getLeague());
        updatedMatch.setHomeTeam(new TeamNestedDto(matchDto.getHomeTeam().getName(), "Country", 1500));
        updatedMatch.setAwayTeam(new TeamNestedDto(matchDto.getAwayTeam().getName(), "Country", 1500));
        updatedMatch.setProbability(matchDto.getProbability());

        when(matchService.updateMatch(eq(id), any(MatchAddUpdateDto.class), any(BindingResult.class))).thenReturn(updatedMatch);

        mockMvc.perform(put("/matchmetrics/api/v0/matches/update/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(matchDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value(updatedMatch.getDate().getTime()))
                .andExpect(jsonPath("$.league").value(updatedMatch.getLeague()))
                .andExpect(jsonPath("$.homeTeam.name").value(updatedMatch.getHomeTeam().getName()))
                .andExpect(jsonPath("$.awayTeam.name").value(updatedMatch.getAwayTeam().getName()))
                .andExpect(jsonPath("$.probability.homeTeamWin").value(updatedMatch.getProbability().getHomeTeamWin()))
                .andExpect(jsonPath("$.probability.draw").value(updatedMatch.getProbability().getDraw()))
                .andExpect(jsonPath("$.probability.awayTeamWin").value(updatedMatch.getProbability().getAwayTeamWin()));
    }

    @Test
    public void testAddMatch() throws Exception {
        MatchAddUpdateDto matchDto = new MatchAddUpdateDto();
        matchDto.setDate("2022-01-01");
        matchDto.setLeague("LeagueA");
        matchDto.setHomeTeam(new TeamNameDto("Test1"));
        matchDto.setAwayTeam(new TeamNameDto("Test2"));
        matchDto.setProbability(new ProbabilityMainDto(0.4f, 0.3f, 0.3f));

        MatchMainDto addedMatch = new MatchMainDto();
        addedMatch.setDate(new SimpleDateFormat("yyyy-MM-dd").parse(matchDto.getDate()));
        addedMatch.setLeague(matchDto.getLeague());
        addedMatch.setHomeTeam(new TeamNestedDto(matchDto.getHomeTeam().getName(), "CountryA", 1500));
        addedMatch.setAwayTeam(new TeamNestedDto(matchDto.getAwayTeam().getName(), "CountryB", 1400));
        addedMatch.setProbability(matchDto.getProbability());

        when(matchService.addMatch(any(MatchAddUpdateDto.class), any(BindingResult.class))).thenReturn(addedMatch);

        mockMvc.perform(post("/matchmetrics/api/v0/matches/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(matchDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value(addedMatch.getDate().getTime()))
                .andExpect(jsonPath("$.league").value(addedMatch.getLeague()))
                .andExpect(jsonPath("$.homeTeam.name").value(addedMatch.getHomeTeam().getName()))
                .andExpect(jsonPath("$.awayTeam.name").value(addedMatch.getAwayTeam().getName()))
                .andExpect(jsonPath("$.probability.homeTeamWin").value(addedMatch.getProbability().getHomeTeamWin()))
                .andExpect(jsonPath("$.probability.draw").value(addedMatch.getProbability().getDraw()))
                .andExpect(jsonPath("$.probability.awayTeamWin").value(addedMatch.getProbability().getAwayTeamWin()));
    }

    @Test
    public void testGetMatchById() throws Exception {
        int id = 1;
        MatchMainDto match = new MatchMainDto();
        match.setDate(new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-01"));
        match.setLeague("LeagueA");
        match.setHomeTeam(new TeamNestedDto("Test1", "CountryA", 1500));
        match.setAwayTeam(new TeamNestedDto("Test2", "CountryB", 1400));
        match.setProbability(new ProbabilityMainDto(0.4f, 0.3f, 0.3f));

        when(matchService.getMatchById(id)).thenReturn(match);

        mockMvc.perform(get("/matchmetrics/api/v0/matches/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.date").value(match.getDate().getTime()))
                .andExpect(jsonPath("$.league").value(match.getLeague()))
                .andExpect(jsonPath("$.homeTeam.name").value(match.getHomeTeam().getName()))
                .andExpect(jsonPath("$.awayTeam.name").value(match.getAwayTeam().getName()))
                .andExpect(jsonPath("$.probability.homeTeamWin").value(match.getProbability().getHomeTeamWin()))
                .andExpect(jsonPath("$.probability.draw").value(match.getProbability().getDraw()))
                .andExpect(jsonPath("$.probability.awayTeamWin").value(match.getProbability().getAwayTeamWin()));
    }

    @Test
    public void whenMatchDoesNotExistException_thenReturnsNotFound() throws Exception {
        int nonExistingId = 111;

        when(matchService.getMatchById(nonExistingId)).thenThrow(new MatchDoesNotExistException(nonExistingId));

        mockMvc.perform(get("/matchmetrics/api/v0/matches/{id}", nonExistingId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Match with id " + nonExistingId + " does not exist."));
    }

}

