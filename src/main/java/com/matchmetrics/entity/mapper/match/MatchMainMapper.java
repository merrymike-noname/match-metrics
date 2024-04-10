package com.matchmetrics.entity.mapper.match;

import com.matchmetrics.entity.Match;
import com.matchmetrics.entity.dto.match.MatchMainDto;
import com.matchmetrics.entity.mapper.probability.ProbabilityMainMapper;
import com.matchmetrics.entity.mapper.team.TeamNestedMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = { TeamNestedMapper.class, ProbabilityMainMapper.class },
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface MatchMainMapper {
    MatchMainDto toDto(Match match);
    Match toEntity(MatchMainDto dto);
}
