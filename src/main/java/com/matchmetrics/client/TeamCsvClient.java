package com.matchmetrics.client;

import com.matchmetrics.entity.dto.team.TeamNestedDto;
import com.matchmetrics.exception.TeamDoesNotExistException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Component
public class TeamCsvClient {

    @Value("${api.url}")
    private String apiUrl;
    private String teamName;

    private static final int TEAM_NAME_POSITION = 1;
    private static final int TEAM_COUNTRY_POSITION = 2;
    private static final int ELO_RATING_POSITION = 4;

    public TeamNestedDto getTeamFromRemote(String teamName) {
        this.teamName = teamName.replaceAll(" ", "");
        String content = getTeamData();
        return getTeamFromContent(content);
    }

    private String getTeamData() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate
                .getForEntity(apiUrl + teamName, String.class);
        return response.getBody();
    }

    private TeamNestedDto getTeamFromContent(String content) {
        String lastLine = getLastNonEmptyLine(content);
        assert lastLine != null;
        System.out.println(lastLine);
        String[] currentTeamStats = lastLine.split(",");

        float eloRating;
        try {
            eloRating = Float.parseFloat(currentTeamStats[ELO_RATING_POSITION]);
        } catch (NumberFormatException e) {
            throw new TeamDoesNotExistException(teamName);
        }

        return new TeamNestedDto(
                currentTeamStats[TEAM_NAME_POSITION],
                currentTeamStats[TEAM_COUNTRY_POSITION],
                eloRating
        );
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