package com.matchmetrics.entity.mapper.team;

import com.matchmetrics.entity.Team;
import com.matchmetrics.entity.dto.team.TeamGetDto;
import com.matchmetrics.entity.mapper.match.MatchNestedMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = { MatchNestedMapper.class },
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface TeamGetMapper {
    TeamGetDto toDto(Team team);
    Team toEntity(TeamGetDto teamDto);
}
