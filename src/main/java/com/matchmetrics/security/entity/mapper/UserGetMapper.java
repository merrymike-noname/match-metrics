/*
 * Copyright (c) 2024.
 * Created by Mykhailo Kovalenko
 */

package com.matchmetrics.security.entity.mapper;

import com.matchmetrics.security.entity.User;
import com.matchmetrics.security.entity.dto.UserGetDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserGetMapper {
    UserGetDto toDto(User user);
    User toEntity(UserGetDto dto);
}
