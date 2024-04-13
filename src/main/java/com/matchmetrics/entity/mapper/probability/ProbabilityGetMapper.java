package com.matchmetrics.entity.mapper.probability;

import com.matchmetrics.entity.Probability;
import com.matchmetrics.entity.dto.probability.ProbabilityGetDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProbabilityGetMapper {
    ProbabilityGetDto toDto(Probability probability);
    Probability toEntity(ProbabilityGetDto dto);
}
