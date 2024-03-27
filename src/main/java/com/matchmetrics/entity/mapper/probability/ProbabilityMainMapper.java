package com.matchmetrics.entity.mapper.probability;

import com.matchmetrics.entity.Probability;
import com.matchmetrics.entity.dto.probability.ProbabilityMainDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProbabilityMainMapper {
    ProbabilityMainDto toDto(Probability probability);
    Probability toEntity(ProbabilityMainDto dto);
}
