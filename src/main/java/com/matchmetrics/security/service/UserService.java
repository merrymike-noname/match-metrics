package com.matchmetrics.security.service;

import com.matchmetrics.security.entity.MakeUserAdminResponse;
import com.matchmetrics.security.entity.RegisterRequest;
import com.matchmetrics.security.entity.UpdateUserResponse;
import com.matchmetrics.security.entity.User;
import com.matchmetrics.entity.Team;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();
    User getUserByEmail(String email);
    UpdateUserResponse updateUser(String email, RegisterRequest userDetails, BindingResult result);
    boolean deleteUser(String email);
    Team getFavouriteTeam(String email);
    String getUserName(String email);
    MakeUserAdminResponse makeUserAdmin(String email);
}
