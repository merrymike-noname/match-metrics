package com.matchmetrics.entity.mapper.team;

import com.matchmetrics.entity.Team;
import com.matchmetrics.entity.dto.team.TeamNameDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamNameMapper {
    TeamNameDto toDto(Team team);
    Team toEntity(TeamNameDto dto);
}
