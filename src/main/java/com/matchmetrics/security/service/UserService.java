package com.matchmetrics.security.service;

import com.matchmetrics.security.entity.RegisterRequest;
import com.matchmetrics.security.entity.UserUpdateRequest;
import com.matchmetrics.security.entity.dto.UserGetDto;
import com.matchmetrics.entity.Team;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface UserService {
    List<UserGetDto> getAllUsers();
    UserGetDto getUserByEmail(String email);
    UserGetDto updateUser(String email, UserUpdateRequest userDetails, BindingResult result);
    void deleteUser(String email);
    Team getFavouriteTeam(String email);
    String getUserName(String email);
    UserGetDto makeUserAdmin(String email);
}
