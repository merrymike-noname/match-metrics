package com.matchmetrics.match;

import com.matchmetrics.entity.Match;
import com.matchmetrics.entity.Probability;
import com.matchmetrics.entity.Team;
import com.matchmetrics.entity.dto.match.MatchAddUpdateDto;
import com.matchmetrics.entity.dto.match.MatchGetDto;
import com.matchmetrics.entity.dto.probability.ProbabilityGetDto;
import com.matchmetrics.entity.dto.team.TeamNameDto;
import com.matchmetrics.entity.mapper.match.*;
import com.matchmetrics.entity.mapper.match.MatchGetMapper;
import com.matchmetrics.entity.mapper.match.MatchGetMapperImpl;
import com.matchmetrics.entity.mapper.probability.ProbabilityGetMapper;
import com.matchmetrics.entity.mapper.probability.ProbabilityGetMapperImpl;
import com.matchmetrics.entity.mapper.team.TeamNameMapperImpl;
import com.matchmetrics.entity.mapper.team.TeamNestedMapperImpl;
import com.matchmetrics.util.DateParser;
import com.matchmetrics.util.validator.ProbabilityValidator;
import com.matchmetrics.exception.DateConversionException;
import com.matchmetrics.exception.InvalidDataException;
import com.matchmetrics.exception.MatchDoesNotExistException;
import com.matchmetrics.exception.TeamDoesNotExistException;
import com.matchmetrics.repository.MatchRepository;
import com.matchmetrics.repository.TeamRepository;
import com.matchmetrics.service.implementation.MatchServiceImpl;
import com.matchmetrics.util.BindingResultInspector;
import com.matchmetrics.util.PageableCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatchServiceImplTest {

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private BindingResult bindingResult;

    @Spy
    private final MatchGetMapper matchGetMapper =
            new MatchGetMapperImpl(new TeamNestedMapperImpl(), new ProbabilityGetMapperImpl());

    @Spy
    private final MatchAddUpdateMapper matchAddUpdateMapper =
            new MatchAddUpdateMapperImpl(new TeamNameMapperImpl(), new ProbabilityGetMapperImpl());

    @Spy
    private final ProbabilityGetMapper probabilityGetMapper = new ProbabilityGetMapperImpl();

    @Spy
    private final DateParser dateParser = new DateParser();

    @Spy
    private final ProbabilityValidator probabilityValidator = new ProbabilityValidator();

    @Spy
    private final PageableCreator pageableCreator = new PageableCreator();

    @Spy
    private final BindingResultInspector bindingResultInspector = new BindingResultInspector();

    @InjectMocks
    private MatchServiceImpl matchService;


    @Test
    void testGetAllMatches() {
        Team homeTeam = new Team("HomeTeam", "Country", 1000);
        Team awayTeam = new Team("AwayTeam", "Country", 1000);
        Probability probability = new Probability(0.2f, 0.5f, 0.3f);
        Match match = new Match(1, new Date(), "League", homeTeam, awayTeam, probability);
        List<Match> matches = List.of(match);
        Pageable pageable = PageRequest.of(1, 2, Sort.unsorted());

        when(matchRepository.findAll(pageable)).thenReturn(new PageImpl<>(matches));

        List<MatchGetDto> expected = matches.stream()
                .map(matchGetMapper::toDto)
                .collect(Collectors.toList());

        List<MatchGetDto> actual = matchService.getAllMatches(1, 2, "default");

        assertEquals(expected, actual);
    }

    @Test
    void testGetMatchesByCriteria() throws ParseException {
        Probability probability = new Probability(0.2f, 0.5f, 0.3f);
        Team homeTeam1 = new Team("TeamA", "Country", 1000);
        Team awayTeam1 = new Team("TeamB", "Country", 1000);
        Match match1 = new Match(1, new SimpleDateFormat("yyyy-MM-dd").parse("2024-04-10"),
                "LeagueA", homeTeam1, awayTeam1, probability);
        Team homeTeam2 = new Team("TeamB", "Country", 1000);
        Team awayTeam2 = new Team("TeamA", "Country", 1000);
        Match match2 = new Match(2, new SimpleDateFormat("yyyy-MM-dd").parse("2024-04-11"),
                "LeagueB", homeTeam2, awayTeam2, probability);
        List<Match> matches = List.of(match1, match2);

        when(matchRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(matches));
        List<MatchGetDto> matchesByCriteria = matchService
                .getMatchesByCriteria("TeamA", "TeamB", null, null, 1, 2, "default");
        List<MatchGetDto> expected = matches.stream()
                .map(matchGetMapper::toDto)
                .toList();
        assertEquals(expected, matchesByCriteria);

        matches = List.of(match1);
        when(matchRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(matches));
        matchesByCriteria = matchService
                .getMatchesByCriteria("TeamA", null, null, null, 1, 2, "default");
        expected = matches.stream()
                .map(matchGetMapper::toDto)
                .toList();
        assertEquals(expected, matchesByCriteria);

        matches = List.of(match2);
        when(matchRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(matches));
        matchesByCriteria = matchService
                .getMatchesByCriteria(null, "TeamA", null, null, 1, 2, "default");
        expected = matches.stream()
                .map(matchGetMapper::toDto)
                .toList();
        assertEquals(expected, matchesByCriteria);

        matches = List.of(match1);
        when(matchRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(matches));
        matchesByCriteria = matchService
                .getMatchesByCriteria("TeamA", null, "2024-04-11", null, 1, 2, "default");
        expected = matches.stream()
                .map(matchGetMapper::toDto)
                .toList();
        assertEquals(expected, matchesByCriteria);
    }

    @Test
    void testGetMatchById() {
        int id = 1;
        Team homeTeam = new Team("HomeTeam", "Country", 1000);
        Team awayTeam = new Team("AwayTeam", "Country", 1000);
        Probability probability = new Probability(0.2f, 0.5f, 0.3f);
        Match match = new Match(1, new Date(), "League", homeTeam, awayTeam, probability);
        MatchGetDto expected = matchGetMapper.toDto(match);

        when(matchRepository.findById(id)).thenReturn(Optional.of(match));

        MatchGetDto matchById = matchService.getMatchById(id);
        assertEquals(expected, matchById);
    }

    @Test
    void testGetMatchById_ThrowsException() {
        int id = 1;

        when(matchRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(MatchDoesNotExistException.class, () -> matchService.getMatchById(id));
    }

    @Test
    void testAddMatch() throws ParseException {
        MatchAddUpdateDto matchDto =
                new MatchAddUpdateDto("2024-04-10", "League",
                        new TeamNameDto("HomeTeam"),
                        new TeamNameDto("AwayTeam"),
                        new ProbabilityGetDto(0.2f, 0.5f, 0.3f));
        Probability probability = new Probability(0.2f, 0.5f, 0.3f);
        Team homeTeam = new Team("HomeTeam", "Country", 1000);
        homeTeam.setHomeMatches(new ArrayList<>());
        Team awayTeam = new Team("AwayTeam", "Country", 1000);
        awayTeam.setAwayMatches(new ArrayList<>());
        Match match = new Match(1, new SimpleDateFormat("yyyy-MM-dd").parse("2024-04-10"),
                "League", homeTeam, awayTeam, probability);
        MatchGetDto expected = matchGetMapper.toDto(match);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(teamRepository.findTeamByName("HomeTeam")).thenReturn(Optional.of(homeTeam));
        when(teamRepository.findTeamByName("AwayTeam")).thenReturn(Optional.of(awayTeam));
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        MatchGetDto addedMatch = matchService.addMatch(matchDto, bindingResult);
        assertEquals(expected, addedMatch);
    }

    @Test
    void testAddMatch_ThrowsExceptions() {
        MatchAddUpdateDto matchAddUpdateDto = new MatchAddUpdateDto("2024-04-10", "League",
                new TeamNameDto("HomeTeam"),
                new TeamNameDto("AwayTeam"),
                new ProbabilityGetDto(0.2f, 0.5f, 0.3f));

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors())
                .thenReturn(List.of(new ObjectError("match", "error message")));
        assertThrows(InvalidDataException.class, () -> matchService.addMatch(matchAddUpdateDto, bindingResult));

        when(bindingResult.hasErrors()).thenReturn(false);
        when(teamRepository.findTeamByName(anyString())).thenReturn(Optional.empty());
        assertThrows(TeamDoesNotExistException.class, () -> matchService.addMatch(matchAddUpdateDto, bindingResult));

        matchAddUpdateDto.setDate("2024-04-80");
        assertThrows(DateConversionException.class, () -> matchService.addMatch(matchAddUpdateDto, bindingResult));
    }

    @Test
    void testUpdateMatch() throws ParseException {
        int id = 1;
        MatchAddUpdateDto matchDto =
                new MatchAddUpdateDto("2024-04-10", "League",
                        new TeamNameDto("HomeTeam"),
                        new TeamNameDto("AwayTeam"),
                        new ProbabilityGetDto(0.2f, 0.5f, 0.3f));
        Probability probability = new Probability(0.2f, 0.5f, 0.3f);
        Team homeTeam = new Team("HomeTeam", "Country", 1000);
        homeTeam.setHomeMatches(new ArrayList<>());
        Team awayTeam = new Team("AwayTeam", "Country", 1000);
        awayTeam.setAwayMatches(new ArrayList<>());
        Match match = new Match(id, new SimpleDateFormat("yyyy-MM-dd").parse("2024-04-10"),
                "League", homeTeam, awayTeam, probability);
        MatchGetDto expected = matchGetMapper.toDto(match);

        when(matchRepository.existsById(id)).thenReturn(true);
        when(bindingResult.hasErrors()).thenReturn(false);
        when(matchRepository.findById(id)).thenReturn(Optional.of(match));
        when(teamRepository.findTeamByName("HomeTeam")).thenReturn(Optional.of(homeTeam));
        when(teamRepository.findTeamByName("AwayTeam")).thenReturn(Optional.of(awayTeam));
        when(matchRepository.save(any(Match.class))).thenReturn(match);

        MatchGetDto updatedMatch = matchService.updateMatch(id, matchDto, bindingResult);
        assertEquals(expected, updatedMatch);
    }

    @Test
    void testUpdateMatch_ThrowsExceptions() {
        int id = 1;
        MatchAddUpdateDto matchAddUpdateDto = new MatchAddUpdateDto("2024-04-10", "League",
                new TeamNameDto("HomeTeam"),
                new TeamNameDto("AwayTeam"),
                new ProbabilityGetDto(0.2f, 0.5f, 0.3f));

        when(matchRepository.existsById(id)).thenReturn(false);
        assertThrows(MatchDoesNotExistException.class, () -> matchService.updateMatch(id, matchAddUpdateDto, bindingResult));

        when(matchRepository.existsById(id)).thenReturn(true);
        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors())
                .thenReturn(List.of(new ObjectError("match", "error message")));
        assertThrows(InvalidDataException.class, () -> matchService.updateMatch(id, matchAddUpdateDto, bindingResult));

        when(bindingResult.hasErrors()).thenReturn(false);
        when(matchRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(MatchDoesNotExistException.class, () -> matchService.updateMatch(id, matchAddUpdateDto, bindingResult));

        when(matchRepository.findById(id)).thenReturn(Optional.of(new Match()));
        when(teamRepository.findTeamByName(anyString())).thenReturn(Optional.empty());
        assertThrows(TeamDoesNotExistException.class, () -> matchService.updateMatch(id, matchAddUpdateDto, bindingResult));

        matchAddUpdateDto.setDate("2024-04-80");
        assertThrows(DateConversionException.class, () -> matchService.updateMatch(id, matchAddUpdateDto, bindingResult));
    }

    @Test
    void testDeleteMatch() throws ParseException {
        int id = 1;
        Team homeTeam = new Team("HomeTeam", "Country", 1000);
        Team awayTeam = new Team("AwayTeam", "Country", 1000);
        Match match = new Match(id, new SimpleDateFormat("yyyy-MM-dd").parse("2024-04-11"),
                "League", homeTeam, awayTeam, new Probability());
        homeTeam.setHomeMatches(new ArrayList<>(List.of(match)));
        awayTeam.setAwayMatches(new ArrayList<>(List.of(match)));

        when(matchRepository.findById(id)).thenReturn(Optional.of(match));
        doNothing().when(matchRepository).deleteById(id);
        matchService.deleteMatch(id);
        verify(matchRepository, times(1)).deleteById(id);

        when(matchRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(MatchDoesNotExistException.class, () -> matchService.deleteMatch(id));
    }
}