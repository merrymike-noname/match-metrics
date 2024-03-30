package com.matchmetrics;

import com.matchmetrics.controller.MatchController;
import com.matchmetrics.entity.dto.match.MatchMainDto;
import com.matchmetrics.entity.dto.probability.ProbabilityMainDto;
import com.matchmetrics.entity.dto.team.TeamNestedDto;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@ExtendWith(MockitoExtension.class)
public class MatchControllerTest {

    @Mock
    private MatchService matchService;

    @InjectMocks
    private MatchController matchController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(matchController).build();
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

}