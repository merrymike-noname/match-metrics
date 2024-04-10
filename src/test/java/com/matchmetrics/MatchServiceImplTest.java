package com.matchmetrics;

import com.matchmetrics.entity.Match;
import com.matchmetrics.entity.Probability;
import com.matchmetrics.entity.Team;
import com.matchmetrics.entity.dto.match.MatchAddUpdateDto;
import com.matchmetrics.entity.dto.match.MatchMainDto;
import com.matchmetrics.entity.mapper.match.MatchMainMapper;
import com.matchmetrics.entity.mapper.match.MatchMainMapperImpl;
import com.matchmetrics.entity.mapper.probability.ProbabilityMainMapperImpl;
import com.matchmetrics.entity.mapper.team.TeamNestedMapperImpl;
import com.matchmetrics.exception.InvalidDataException;
import com.matchmetrics.repository.MatchRepository;
import com.matchmetrics.service.implementation.MatchServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MatchServiceImplTest {

    @Mock
    private MatchRepository matchRepository;

    @Spy
    private final MatchMainMapper matchMainMapper =
            new MatchMainMapperImpl(new TeamNestedMapperImpl(), new ProbabilityMainMapperImpl());

    @InjectMocks
    private MatchServiceImpl matchService;

    @Test
    void testUpdateMatchError() {
        int id = 1;
        MatchAddUpdateDto match = new MatchAddUpdateDto();
        BindingResult bindingResult = org.mockito.Mockito.mock(BindingResult.class);

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors())
                .thenReturn(List.of(new ObjectError("match", "error message")));
        when(matchRepository.existsById(id)).thenReturn(true);

        assertThrows(InvalidDataException.class, () -> matchService.updateMatch(id, match, bindingResult));
    }

    @Test
    void testGetAllMatches() {
        Team homeTeam = new Team("HomeTeam", "Country", 1000);
        Team awayTeam = new Team("AwayTeam", "Country", 1000);
        Probability probability = new Probability(0.2f, 0.5f, 0.3f);
        Match match = new Match(1, new Date(), "League", homeTeam, awayTeam, probability);
        List<Match> matches = List.of(match);
        Pageable pageable = PageRequest.of(1, 2, Sort.unsorted());

        when(matchRepository.findAll(pageable)).thenReturn(new PageImpl<>(matches));

        List<MatchMainDto> expected = matches.stream()
                .map(matchMainMapper::toDto)
                .collect(Collectors.toList());

        List<MatchMainDto> actual = matchService.getAllMatches(1, 2, "default");

        assertEquals(expected, actual);
    }
}