package com.matchmetrics.entity.mapper.nested;

import com.matchmetrics.entity.Team;
import com.matchmetrics.entity.dto.nested.TeamNestedDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TeamNestedMapper {
    TeamNestedDto toDto(Team team);
    Team toEntity(TeamNestedDto dto);
}
