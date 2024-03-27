package com.matchmetrics.entity.mapper.main;

import com.matchmetrics.entity.Probability;
import com.matchmetrics.entity.dto.main.ProbabilityMainDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ProbabilityMainMapper {
    ProbabilityMainDto toDto(Probability probability);
    Probability toEntity(ProbabilityMainDto dto);
}
