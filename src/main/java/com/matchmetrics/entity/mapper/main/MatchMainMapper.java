package com.matchmetrics.entity.mapper.main;

import com.matchmetrics.entity.Match;
import com.matchmetrics.entity.dto.main.MatchMainDto;
import com.matchmetrics.entity.mapper.nested.TeamNestedMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = { TeamNestedMapper.class, ProbabilityMainMapper.class })
public interface MatchMainMapper {
    MatchMainDto toDto(Match match);
    Match toEntity(MatchMainDto dto);
}
