package com.matchmomentummetrics.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class EloCsvClient {

    private static final String API_URL = "http://api.clubelo.com/";
    private static final int ELO_RATING_POSITION = 4;

    public float getTeamElo(String teamName) {
        String content = getTeamEloData(teamName);
        return getEloFromContent(content);
    }

    private String getTeamEloData(String teamName) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate
                .getForEntity(API_URL + teamName, String.class);
        return response.getBody();
    }

    private float getEloFromContent(String content) {
        String lastLine = getLastNonEmptyLine(content);
        assert lastLine != null;
        System.out.println(lastLine);
        String[] currentTeamStats = lastLine.split(",");
        return Float.parseFloat(currentTeamStats[ELO_RATING_POSITION]);
    }

    private String getLastNonEmptyLine(String content) {
        assert content != null;
        List<String> lines = Arrays.asList(content.split("\n"));
        for (int i = lines.size() - 1; i >= 0; i--) {
            String line = lines.get(i);
            if (!line.isBlank()) {
                return line;
            }
        }
        return null;
    }

}
