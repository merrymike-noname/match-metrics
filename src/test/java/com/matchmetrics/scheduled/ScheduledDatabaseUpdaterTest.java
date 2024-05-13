package com.matchmetrics.scheduled;

import com.matchmetrics.client.FixturesCsvClient;
import com.matchmetrics.entity.Match;
import com.matchmetrics.entity.Probability;
import com.matchmetrics.entity.Team;
import com.matchmetrics.entity.dto.match.MatchAddUpdateDto;
import com.matchmetrics.entity.dto.probability.ProbabilityGetDto;
import com.matchmetrics.entity.dto.team.TeamNameDto;
import com.matchmetrics.entity.mapper.match.MatchAddUpdateMapper;
import com.matchmetrics.entity.mapper.match.MatchAddUpdateMapperImpl;
import com.matchmetrics.entity.mapper.probability.ProbabilityGetMapperImpl;
import com.matchmetrics.entity.mapper.team.TeamNameMapperImpl;
import com.matchmetrics.repository.MatchRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.Date;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class ScheduledDatabaseUpdaterTest {

    @Spy
    private final MatchAddUpdateMapper matchAddUpdateMapper =
            new MatchAddUpdateMapperImpl(new TeamNameMapperImpl(), new ProbabilityGetMapperImpl());

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testUpdateDatabase() {
        Team team1 = new Team("A", "UK", 100);
        Team team2 = new Team("B", "UK", 200);

        Probability probability1 = new Probability(0.5f, 0.3f, 0.2f);

        Match match1 = new Match(1, matchAddUpdateMapper.convertStringToDate("2022-12-01"), "league1", team1, team2, probability1);
        Match match2 = new Match(2, matchAddUpdateMapper.convertStringToDate("2022-12-02"), "league2", team2, team1, probability1);
        List<Match> allMatchesOld = Arrays.asList(match1, match2);

        TeamNameDto teamNameDto1 = new TeamNameDto("A");
        TeamNameDto teamNameDto2 = new TeamNameDto("B");
        TeamNameDto teamNameDto3 = new TeamNameDto("C");

        ProbabilityGetDto probabilityGetDto1 = new ProbabilityGetDto(0.5f, 0.3f, 0.2f);
        ProbabilityGetDto probabilityGetDto2 = new ProbabilityGetDto(0.6f, 0.2f, 0.2f);

        MatchAddUpdateDto dto1 = new MatchAddUpdateDto("2022-12-01", "league1", teamNameDto1, teamNameDto2, probabilityGetDto1);
        MatchAddUpdateDto dto2 = new MatchAddUpdateDto("2022-12-02", "league2", teamNameDto2, teamNameDto1, probabilityGetDto2);
        MatchAddUpdateDto dto3 = new MatchAddUpdateDto("2022-12-03", "league3", teamNameDto3, teamNameDto1, probabilityGetDto2);
        List<MatchAddUpdateDto> allMatchesNew = new ArrayList<>(Arrays.asList(dto1, dto2, dto3));

        allMatchesNew.removeIf(newMatch -> allMatchesOld.stream()
                .anyMatch(oldMatch -> newMatch.equals(matchAddUpdateMapper.toDto(oldMatch))));

        assertThat(allMatchesNew).containsExactly(dto3);
    }
}