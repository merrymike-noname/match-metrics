package com.matchmetrics.team;

import com.matchmetrics.entity.Team;
import com.matchmetrics.entity.dto.team.TeamGetDto;
import com.matchmetrics.entity.dto.team.TeamNestedDto;
import com.matchmetrics.entity.mapper.team.TeamGetMapper;
import com.matchmetrics.entity.mapper.team.TeamNestedMapper;
import com.matchmetrics.repository.TeamRepository;
import com.matchmetrics.service.implementation.TeamServiceImpl;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeamServiceImplTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private TeamGetMapper teamGetMapper;

    @Spy
    private TeamNestedMapper teamNestedMapper;

    @InjectMocks
    private TeamServiceImpl teamService;

    @Test
    void testGetAllTeams() {
        Team team = new Team("TeamName", "Country", 1000);
        List<Team> teams = List.of(team);
        Pageable pageable = PageRequest.of(1, 3, Sort.unsorted());

        when(teamRepository.findAll(pageable)).thenReturn(new PageImpl<>(teams));

        List<TeamGetDto> expected = teams.stream()
                .map(teamGetMapper::toDto)
                .collect(Collectors.toList());

        List<TeamGetDto> actual = teamService.getAllTeams(1, 3, "default");

        assertEquals(expected, actual);
    }

}