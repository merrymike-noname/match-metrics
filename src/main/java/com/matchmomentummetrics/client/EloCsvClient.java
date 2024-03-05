package com.matchmomentummetrics.client;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class EloCsvClient {
    public float getTeamElo(String teamName) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate
                .getForEntity("http://api.clubelo.com/" + teamName, String.class);
        String content = response.getBody();

        String lastLine = null;
        String currentLine = null;
        assert content != null;
        int lastLineIndex = content.length();
        while (lastLineIndex != -1) {
            lastLineIndex = content.lastIndexOf("\n" , lastLineIndex - 1);
            if (lastLineIndex > -1) {
                currentLine = content.substring(lastLineIndex + 1);
            }
            if (currentLine!= null && !currentLine.isBlank()) {
                lastLine = currentLine.trim();
                break;
            }
        }
        assert lastLine != null;
        String[] currentTeamStats = lastLine.split(",");

        return Float.parseFloat(currentTeamStats[4]);
    }

}
