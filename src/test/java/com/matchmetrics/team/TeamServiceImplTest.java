package com.matchmetrics.team;

import com.matchmetrics.client.TeamCsvClient;
import com.matchmetrics.entity.Team;
import com.matchmetrics.entity.dto.team.TeamGetDto;
import com.matchmetrics.entity.dto.team.TeamNestedDto;
import com.matchmetrics.entity.mapper.match.MatchNestedMapperImpl;
import com.matchmetrics.entity.mapper.team.TeamGetMapper;
import com.matchmetrics.entity.mapper.team.TeamGetMapperImpl;
import com.matchmetrics.entity.mapper.team.TeamNestedMapper;
import com.matchmetrics.entity.mapper.team.TeamNestedMapperImpl;
import com.matchmetrics.exception.InvalidDataException;
import com.matchmetrics.exception.TeamAlreadyExistsException;
import com.matchmetrics.exception.TeamDoesNotExistException;
import com.matchmetrics.repository.TeamRepository;
import com.matchmetrics.service.implementation.TeamServiceImpl;
import com.matchmetrics.util.BindingResultInspector;
import com.matchmetrics.util.PageableCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceImplTest {
    @Mock
    private TeamRepository teamRepository;

    @Mock
    private BindingResult bindingResult;

    @Mock
    private TeamCsvClient teamCsvClient;

    @Spy
    private TeamGetMapper teamGetMapper = new TeamGetMapperImpl(new MatchNestedMapperImpl());

    @Spy
    private TeamNestedMapper teamNestedMapper = new TeamNestedMapperImpl();

    @Spy
    private final PageableCreator pageableCreator = new PageableCreator();

    @Spy
    private final BindingResultInspector bindingResultInspector = new BindingResultInspector();

    @InjectMocks
    private TeamServiceImpl teamService;

    @Test
    void testGetAllTeams() {
        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            teams.add(new Team());
        }
        List<TeamGetDto> expectedTeams = teams.stream().map(teamGetMapper::toDto).toList();

        when(teamRepository.findAll(any(Pageable.class))).thenReturn(new PageImpl<>(teams));

        List<TeamGetDto> actualTeams = teamService.getAllTeams(1, 5, "default");
        assertThat(actualTeams).isEqualTo(expectedTeams);
    }

    @Test
    void testGetTeamsByCriteria() {
        Team team1 = new Team("TeamA", "Country", 1000f);
        Team team2 = new Team("TeamB", "Country", 2000f);
        Team team3 = new Team("TeamC", "Country", 1500f);
        List<Team> teams = List.of(team1, team2, team3);
        List<TeamGetDto> expectedTeams;
    
        when(teamRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(teams));
        expectedTeams = teams.stream().map(teamGetMapper::toDto).toList();

        List<TeamGetDto> actualTeams = teamService.getTeamsByCriteria(null,
                "Country", null, 1, 5, "default");
        verify(teamRepository, times(1)).findAll(any(Specification.class), any(Pageable.class));
        assertThat(actualTeams).isEqualTo(expectedTeams);
    }

    @Test
    void testGetTeamById() {
        int id = 1;
        Team team = new Team("Team", "Country", 1000f);
        TeamGetDto expectedTeam = teamGetMapper.toDto(team);

        when(teamRepository.findById(id)).thenReturn(Optional.of(team));

        TeamGetDto actualTeam = teamService.getTeamById(id);
        assertThat(actualTeam).isEqualTo(expectedTeam);
    }

    @Test
    void testGetTeamById_ThrowsException() {
        int id = 1;

        when(teamRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TeamDoesNotExistException.class, () -> teamService.getTeamById(id));
    }

    @Test
    void testAddTeam() {
        Team teamEntity = new Team("Team", "Country", 1000f);
        TeamNestedDto dto = new TeamNestedDto("Team", "Country", 1000f);
        TeamGetDto expected = teamGetMapper.toDto(teamEntity);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(teamRepository.existsByName("Team")).thenReturn(false);
        when(teamRepository.save(any(Team.class))).thenReturn(teamEntity);

        TeamGetDto createdTeam = teamService.createTeam(dto, bindingResult);
        assertThat(createdTeam).isEqualTo(expected);
    }

    @Test
    void testAddTeam_ThrowsExceptions() {
        TeamNestedDto dto = new TeamNestedDto("Team", "Country", 1000f);

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors())
                .thenReturn(List.of(new ObjectError("team", "error message")));
        assertThrows(InvalidDataException.class, () -> teamService.createTeam(dto, bindingResult));

        when(bindingResult.hasErrors()).thenReturn(false);
        when(teamRepository.existsByName(dto.getName())).thenReturn(true);
        assertThrows(TeamAlreadyExistsException.class, () -> teamService.createTeam(dto, bindingResult));
    }

    @Test
    void testUpdateTeam() {
        int id = 1;
        TeamNestedDto dto = new TeamNestedDto("Team", "Country", 1200f);
        Team existingTeam = new Team("Team1", "Country", 1000f);
        Team updatedTeam = teamNestedMapper.toEntity(dto);
        TeamGetDto expected = teamGetMapper.toDto(updatedTeam);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(teamRepository.findById(id)).thenReturn(Optional.of(existingTeam));
        when(teamRepository.save(any(Team.class))).thenReturn(updatedTeam);

        TeamGetDto actualTeam = teamService.updateTeam(id, dto, bindingResult);
        assertThat(actualTeam).isEqualTo(expected);
    }

    @Test
    void testUpdateTeam_ThrowsExceptions() {
        int id = 1;
        TeamNestedDto dto = new TeamNestedDto("Team", "Country", 1000f);
        Team existingTeam = new Team("Team1", "Country", 1000f);

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getAllErrors())
                .thenReturn(List.of(new ObjectError("team", "error message")));
        assertThrows(InvalidDataException.class, () -> teamService.updateTeam(id, dto, bindingResult));

        when(bindingResult.hasErrors()).thenReturn(false);
        when(teamRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(TeamDoesNotExistException.class, () -> teamService.updateTeam(id, dto, bindingResult));

        when(teamRepository.findById(id)).thenReturn(Optional.of(existingTeam));
        when(teamRepository.existsByName(dto.getName())).thenReturn(true);
        assertThrows(TeamAlreadyExistsException.class, () -> teamService.updateTeam(id, dto, bindingResult));
    }

    @Test
    void testDeleteTeam() {
        int id = 1;
        when(teamRepository.existsById(id)).thenReturn(true);
        doNothing().when(teamRepository).deleteById(id);
        teamService.deleteTeam(id);
        verify(teamRepository, times(1)).deleteById(id);

        when(teamRepository.existsById(id)).thenReturn(false);
        assertThrows(TeamDoesNotExistException.class, () -> teamService.deleteTeam(id));
    }

}
