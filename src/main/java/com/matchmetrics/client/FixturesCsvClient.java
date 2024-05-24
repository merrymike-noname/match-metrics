package com.matchmetrics.client;

import com.matchmetrics.entity.dto.match.MatchAddUpdateDto;
import com.matchmetrics.entity.dto.probability.ProbabilityGetDto;
import com.matchmetrics.entity.dto.team.TeamNameDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@Component
public class FixturesCsvClient {
    @Value("${api.url}")
    private String apiUrl;

    public List<MatchAddUpdateDto> getFixtures() {
        List<MatchAddUpdateDto> matches;
        String content = getFixturesData();
        matches = getMatchesFromContent(content);
        return matches;
    }

    private String getFixturesData() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate
                .getForEntity(apiUrl + "Fixtures", String.class);
        return response.getBody();
    }

    private List<MatchAddUpdateDto> getMatchesFromContent(String content) {
        Scanner scanner = new Scanner(content.trim());
        List<MatchAddUpdateDto> matches = new ArrayList<>();

        if (scanner.hasNextLine()) {
            scanner.nextLine();
        }

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            matches.add(parseLine(line));
        }

        scanner.close();

        return matches;
    }

    private MatchAddUpdateDto parseLine(String line) {
        MatchAddUpdateDto match = new MatchAddUpdateDto();
        String[] fields = line.split(",");

        match.setDate(fields[0]);
        match.setLeague(fields[1]);
        match.setHomeTeam(new TeamNameDto(fields[2]));
        match.setAwayTeam(new TeamNameDto(fields[3]));
        match.setProbability(computeProbabilities(fields));

        return match;
    }

    private ProbabilityGetDto computeProbabilities(String[] fields) {
        ProbabilityGetDto probability = new ProbabilityGetDto();
        BigDecimal awayTeamWin = BigDecimal.ZERO;
        BigDecimal draw;
        BigDecimal homeTeamWin;

        for (int i = 4; i < 10; i++) {
            awayTeamWin = awayTeamWin.add(new BigDecimal(fields[i]));
        }
        draw = new BigDecimal(fields[10]);
        homeTeamWin = BigDecimal.ONE.subtract(awayTeamWin).subtract(draw);

        probability.setHomeTeamWin(homeTeamWin.setScale(6, RoundingMode.HALF_UP).floatValue());
        probability.setDraw(draw.setScale(6, RoundingMode.HALF_UP).floatValue());
        probability.setAwayTeamWin(awayTeamWin.setScale(6, RoundingMode.HALF_UP).floatValue());

        return probability;
    }

}
