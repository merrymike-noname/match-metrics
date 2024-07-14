package com.matchmetrics.scheduled;

import com.matchmetrics.client.FixturesCsvClient;
import com.matchmetrics.client.TeamCsvClient;
import com.matchmetrics.entity.Match;
import com.matchmetrics.entity.Probability;
import com.matchmetrics.entity.Team;
import com.matchmetrics.entity.dto.match.MatchAddUpdateDto;
import com.matchmetrics.entity.dto.team.TeamNestedDto;
import com.matchmetrics.entity.mapper.match.MatchAddUpdateMapper;
import com.matchmetrics.entity.mapper.probability.ProbabilityGetMapper;
import com.matchmetrics.entity.mapper.team.TeamNestedMapper;
import com.matchmetrics.repository.MatchRepository;
import com.matchmetrics.repository.TeamRepository;
import com.matchmetrics.util.validator.ProbabilityValidator;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class FixturesUpdater {

    private final FixturesCsvClient fixturesCsvClient;
    private final TeamCsvClient teamCsvClient;
    private final ProbabilityValidator probabilityValidator;
    private final MatchAddUpdateMapper addUpdateMapper;
    private final TeamNestedMapper teamNestedMapper;
    private final ProbabilityGetMapper probabilityGetMapper;

    private final MatchRepository matchRepository;
    private final TeamRepository teamRepository;
    private final Logger logger = LoggerFactory.getLogger(FixturesUpdater.class);

    public FixturesUpdater(FixturesCsvClient fixturesCsvClient,
                           TeamCsvClient teamCsvClient,
                           ProbabilityValidator probabilityValidator,
                           MatchAddUpdateMapper addUpdateMapper,
                           TeamNestedMapper teamNestedMapper,
                           ProbabilityGetMapper probabilityGetMapper,
                           MatchRepository matchRepository,
                           TeamRepository teamRepository) {
        this.fixturesCsvClient = fixturesCsvClient;
        this.teamCsvClient = teamCsvClient;
        this.probabilityValidator = probabilityValidator;
        this.addUpdateMapper = addUpdateMapper;
        this.teamNestedMapper = teamNestedMapper;
        this.probabilityGetMapper = probabilityGetMapper;
        this.matchRepository = matchRepository;
        this.teamRepository = teamRepository;
    }

    //@Scheduled(cron = "0 30 3 * * *")
    //@Retryable(value = { ResourceAccessException.class }, maxAttempts = 10, backoff = @Backoff(delay = 5000))
    @Transactional
    public void updateFixtures() {
        logger.info("Database update started");
        Date today = new Date();
        List<Match> allMatchesOld = matchRepository.findAll();
        List<Team> teamsToUpdate = new ArrayList<>();
        List<MatchAddUpdateDto> allMatchesNew = fixturesCsvClient.getFixtures();

        for (Match m : allMatchesOld) {

            allMatchesNew.removeIf(newMatch -> allMatchesOld.stream()
                    .anyMatch(oldMatch -> newMatch.equals(addUpdateMapper.toDto(oldMatch))));

            // removes all outdated matches
            if (m.getDate().before(today)) {
                m.getHomeTeam().getHomeMatches().remove(m);
                m.getAwayTeam().getAwayMatches().remove(m);
                teamsToUpdate.add(m.getHomeTeam());
                teamsToUpdate.add(m.getAwayTeam());
                matchRepository.delete(m);
            }
        }
        logger.info("Persisting new matches");
        persistNewMatches(allMatchesNew);

        logger.info("Updating teams elo");
        updateTeamInfo(teamsToUpdate);

        logger.info("Database update finished");
    }

    @Transactional
    protected void persistNewMatches(List<MatchAddUpdateDto> allMatchesNew) {
        for (MatchAddUpdateDto m : allMatchesNew) {
            Optional<Team> homeTeamOptional = teamRepository.findTeamByName(m.getHomeTeam().getName());
            if (homeTeamOptional.isEmpty()) {
                TeamNestedDto teamFromRemote = teamCsvClient.getTeamFromRemote(m.getHomeTeam().getName());
                Team homeTeam = teamRepository.save(teamNestedMapper.toEntity(teamFromRemote));
                homeTeamOptional = Optional.of(homeTeam);
            }

            Optional<Team> awayTeamOptional = teamRepository.findTeamByName(m.getAwayTeam().getName());
            if (awayTeamOptional.isEmpty()) {
                TeamNestedDto teamFromRemote = teamCsvClient.getTeamFromRemote(m.getAwayTeam().getName());
                Team awayTeam = teamRepository.save(teamNestedMapper.toEntity(teamFromRemote));
                awayTeamOptional = Optional.of(awayTeam);
            }

            probabilityValidator.validateProbability(m.getProbability());
            Probability probability = probabilityGetMapper.toEntity(m.getProbability());

            Match newMatch = addUpdateMapper.toEntity(m);
            newMatch.setHomeTeam(homeTeamOptional.get());
            newMatch.setAwayTeam(awayTeamOptional.get());
            newMatch.setProbability(probability);

            matchRepository.save(newMatch);
        }
        logger.info("Match persisting is finished");
    }

    @Transactional
    protected void updateTeamInfo(List<Team> teamsToUpdate) {
        logger.info("Teams to update list size: {}", teamsToUpdate.size());
        for (Team teamToUpdate : teamsToUpdate) {
            TeamNestedDto teamFromRemote = teamCsvClient.getTeamFromRemote(teamToUpdate.getName());
            if (teamToUpdate.getElo() != teamFromRemote.getElo()) {
                teamToUpdate.setElo(teamFromRemote.getElo());
                teamRepository.save(teamToUpdate);
            }
        }
    }
}