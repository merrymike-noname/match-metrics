package com.matchmetrics.probability;

import com.matchmetrics.controller.ProbabilityController;
import com.matchmetrics.controller.controller_advice.GlobalExceptionHandler;
import com.matchmetrics.entity.dto.probability.ProbabilityGetDto;
import com.matchmetrics.service.ProbabilityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class ProbabilityControllerTest {

    @InjectMocks
    private ProbabilityController probabilityController;

    @MockBean
    private ProbabilityService probabilityService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        probabilityController = new ProbabilityController(probabilityService);
        mockMvc = MockMvcBuilders.standaloneSetup(probabilityController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    public void testGetAllProbabilities() throws Exception {
        ProbabilityGetDto probability1 = new ProbabilityGetDto(0.5f, 0.3f, 0.2f);
        ProbabilityGetDto probability2 = new ProbabilityGetDto(0.4f, 0.3f, 0.3f);
        List<ProbabilityGetDto> expectedProbabilities = Arrays.asList(probability1, probability2);

        when(probabilityService.getAllProbabilities(0, 3, "default")).thenReturn(expectedProbabilities);

        mockMvc.perform(get("/matchmetrics/api/v0/probabilities/all")
                        .param("page", "1")
                        .param("perPage", "3")
                        .param("sortBy", "default")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[{" +
                        "\"homeTeamWin\":" + probability1.getHomeTeamWin() + "," +
                        "\"draw\":" + probability1.getDraw() + "," +
                        "\"awayTeamWin\":" + probability1.getAwayTeamWin() +
                        "},{" +
                        "\"homeTeamWin\":" + probability2.getHomeTeamWin() + "," +
                        "\"draw\":" + probability2.getDraw() + "," +
                        "\"awayTeamWin\":" + probability2.getAwayTeamWin() +
                        "}]"));

        verify(probabilityService, times(1)).getAllProbabilities(0, 3, "default");
    }

    @Test
    public void testGetProbabilityById() throws Exception {
        ProbabilityGetDto probability = new ProbabilityGetDto(0.5f, 0.3f, 0.2f);
        when(probabilityService.getProbabilityById(1)).thenReturn(probability);

        mockMvc.perform(get("/matchmetrics/api/v0/probabilities/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(probabilityService, times(1)).getProbabilityById(1);
    }

    @Test
    public void testGetProbabilitiesByCriteria() throws Exception {
        ProbabilityGetDto probability1 = new ProbabilityGetDto(0.5f, 0.3f, 0.2f);
        ProbabilityGetDto probability2 = new ProbabilityGetDto(0.4f, 0.3f, 0.3f);
        List<ProbabilityGetDto> expectedProbabilities = Arrays.asList(probability1, probability2);

        when(probabilityService.getProbabilitiesByCriteria(0.5f, true, 0, 3, "default")).thenReturn(expectedProbabilities);

        mockMvc.perform(get("/matchmetrics/api/v0/probabilities")
                        .param("probability", "0.5")
                        .param("higher", "true")
                        .param("page", "1")
                        .param("perPage", "3")
                        .param("sortBy", "default")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(probabilityService, times(1)).getProbabilitiesByCriteria(0.5f, true, 0, 3, "default");
    }

    @Test
    public void testCreateProbability() throws Exception {
        ProbabilityGetDto probability = new ProbabilityGetDto(0.5f, 0.3f, 0.2f);
        when(probabilityService.createProbability(any(ProbabilityGetDto.class), any())).thenReturn(probability);

        mockMvc.perform(post("/matchmetrics/api/v0/probabilities/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"homeTeamWin\":0.5,\"draw\":0.3,\"awayTeamWin\":0.2}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(probabilityService, times(1)).createProbability(any(ProbabilityGetDto.class), any());
    }

    @Test
    public void testUpdateProbability() throws Exception {
        ProbabilityGetDto probability = new ProbabilityGetDto(0.5f, 0.3f, 0.2f);
        when(probabilityService.updateProbability(eq(1), any(ProbabilityGetDto.class), any())).thenReturn(probability);

        mockMvc.perform(put("/matchmetrics/api/v0/probabilities/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"homeTeamWin\":0.5,\"draw\":0.3,\"awayTeamWin\":0.2}")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(probabilityService, times(1)).updateProbability(eq(1), any(ProbabilityGetDto.class), any());
    }

    @Test
    public void testDeleteProbability() throws Exception {
        doNothing().when(probabilityService).deleteProbability(1);

        mockMvc.perform(delete("/matchmetrics/api/v0/probabilities/delete/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(probabilityService, times(1)).deleteProbability(1);
    }

}