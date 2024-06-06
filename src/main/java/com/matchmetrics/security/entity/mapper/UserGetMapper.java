/*
 * Copyright (c) 2024.
 * Created by Mykhailo Kovalenko
 */

package com.matchmetrics.security.entity.mapper;

import com.matchmetrics.entity.Team;
import com.matchmetrics.security.entity.User;
import com.matchmetrics.security.entity.dto.UserGetDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class UserGetMapper {

    @Mapping(target = "favouriteTeam", expression = "java( extractTeamName(user.getFavouriteTeam()) )")
    public abstract UserGetDto toDto(User user);
    @Mapping(target = "favouriteTeam", expression = "java( getTeamFromName() )")
    public abstract User toEntity(UserGetDto dto);

    public String extractTeamName(Team team) {
        return team.getName();
    }

    public Team getTeamFromName() {
        return null;
    }
}
