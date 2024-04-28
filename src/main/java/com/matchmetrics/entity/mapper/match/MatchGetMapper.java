package com.matchmetrics.entity.mapper.match;

import com.matchmetrics.entity.Match;
import com.matchmetrics.entity.dto.match.MatchGetDto;
import com.matchmetrics.entity.mapper.probability.ProbabilityGetMapper;
import com.matchmetrics.entity.mapper.team.TeamNestedMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = { TeamNestedMapper.class, ProbabilityGetMapper.class },
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface MatchGetMapper {
    MatchGetDto toDto(Match match);
    Match toEntity(MatchGetDto dto);
}
