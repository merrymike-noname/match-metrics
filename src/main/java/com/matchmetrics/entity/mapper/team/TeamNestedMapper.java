package com.matchmetrics.entity.mapper.team;

import com.matchmetrics.entity.Team;
import com.matchmetrics.entity.dto.team.TeamNestedDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamNestedMapper {
    TeamNestedDto toDto(Team team);
    Team toEntity(TeamNestedDto dto);
}
