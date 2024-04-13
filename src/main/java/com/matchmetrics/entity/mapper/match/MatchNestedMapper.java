package com.matchmetrics.entity.mapper.match;

import com.matchmetrics.entity.Match;
import com.matchmetrics.entity.dto.match.MatchNestedDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MatchNestedMapper {
    MatchNestedDto toDto(Match match);
    Match toEntity(MatchNestedDto matchNestedDto);
}
