package com.matchmetrics.scheduled;

import com.matchmetrics.client.FixturesCsvClient;
import com.matchmetrics.client.TeamCsvClient;
import com.matchmetrics.entity.dto.match.MatchAddUpdateDto;
import com.matchmetrics.entity.dto.team.TeamNestedDto;
import com.matchmetrics.entity.mapper.team.TeamNestedMapper;
import com.matchmetrics.repository.TeamRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.List;

@Component
public class DatabaseUpdater {

    private final FixturesCsvClient fixturesCsvClient;
    private final TeamCsvClient teamCsvClient;
    private final TeamRepository teamRepository;
    private final TeamNestedMapper teamNestedMapper;
    private final FixturesUpdater fixturesUpdater;
    private final Logger logger = LoggerFactory.getLogger(DatabaseUpdater.class);

    public DatabaseUpdater(
            FixturesCsvClient fixturesCsvClient,
            TeamCsvClient teamCsvClient,
            TeamRepository teamRepository,
            TeamNestedMapper teamNestedMapper,
            FixturesUpdater fixturesUpdater)
    {
        this.fixturesCsvClient = fixturesCsvClient;
        this.teamCsvClient = teamCsvClient;
        this.teamRepository = teamRepository;
        this.teamNestedMapper = teamNestedMapper;
        this.fixturesUpdater = fixturesUpdater;
    }

    @Scheduled(cron = "0 30 3 * * *")
    @Retryable(value = { ResourceAccessException.class }, maxAttempts = 10, backoff = @Backoff(delay = 5000))
    public void fillTeamDb() {
        logger.info("Filling team database");
        List<MatchAddUpdateDto> allMatchesNew = fixturesCsvClient.getFixtures();
        List<String> teamsToAdd = new ArrayList<>();
        for (MatchAddUpdateDto m : allMatchesNew) {
            teamsToAdd.add(m.getHomeTeam().getName());
            teamsToAdd.add(m.getAwayTeam().getName());
        }
        logger.info("Teams list length: {}", teamsToAdd.size());
        logger.info("Adding new teams to database");
        int i = 0;
        for (String teamName : teamsToAdd) {
            logger.info("Adding new team {}, request #{}", teamName, ++i);
            addTeamToTeamDb(teamName);
        }
        logger.info("Finished adding teams");

        fixturesUpdater.updateFixtures();
    }

    @Transactional
    protected void addTeamToTeamDb(String name) {
        if (!teamRepository.existsByName(name)) {
            TeamNestedDto teamDto = teamCsvClient.getTeamFromRemote(name);
            teamRepository.save(teamNestedMapper.toEntity(teamDto));
        }
    }
}