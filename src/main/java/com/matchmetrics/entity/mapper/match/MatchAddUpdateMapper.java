package com.matchmetrics.entity.mapper.match;

import com.matchmetrics.entity.Match;
import com.matchmetrics.entity.dto.match.MatchAddUpdateDto;
import com.matchmetrics.entity.mapper.probability.ProbabilityGetMapper;
import com.matchmetrics.entity.mapper.team.TeamNameMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Mapper(componentModel = "spring",
        uses = { TeamNameMapper.class, ProbabilityGetMapper.class },
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class MatchAddUpdateMapper {
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public abstract MatchAddUpdateDto toDto(Match match);

    @Mapping(target = "date", expression = "java( convertStringToDate(dto.getDate()) )")
    public abstract Match toEntity(MatchAddUpdateDto dto);

    public Date convertStringToDate(String dateString) {
        if (dateString == null) {
            return null;
        } else {
            // Parse string date to Date type
            LocalDate localDate = LocalDate.parse(dateString, dateTimeFormatter);
            return java.sql.Date.valueOf(localDate);
        }
    }
}
