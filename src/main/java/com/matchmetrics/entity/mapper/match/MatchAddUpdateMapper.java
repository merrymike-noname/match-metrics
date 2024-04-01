package com.matchmetrics.entity.mapper.match;

import com.matchmetrics.entity.Match;
import com.matchmetrics.entity.dto.match.MatchAddUpdateDto;
import com.matchmetrics.entity.mapper.probability.ProbabilityMainMapper;
import com.matchmetrics.entity.mapper.team.TeamNameMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring",
        uses = { TeamNameMapper.class, ProbabilityMainMapper.class })
public interface MatchAddUpdateMapper {
    MatchAddUpdateDto toDto(Match match);
    Match toEntity(MatchAddUpdateDto dto);
}
