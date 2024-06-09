package com.matchmetrics.security.controller;

import com.matchmetrics.security.entity.RegisterRequest;
import com.matchmetrics.entity.Team;
import com.matchmetrics.security.entity.dto.UserGetDto;
import com.matchmetrics.security.service.UserService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("matchmetrics/api/v0/users")
public class UserController {

    private final UserService userService;
    private final Logger logger = LoggerFactory.getLogger(UserController.class);

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<UserGetDto> getAllUsers() {
        logger.info("Fetching all users");
        return userService.getAllUsers();
    }

    @GetMapping("/{email}")
    public UserGetDto getUserByEmail(@PathVariable String email) {
        logger.info("Fetching user by email: {}", email);
        return userService.getUserByEmail(email);
    }

    @PutMapping("/update/{email}")
    public UserGetDto updateUser(
            @PathVariable String email,
            @Valid @RequestBody RegisterRequest userDetails,
            BindingResult result) {
        logger.info("Updating user with email: {}", email);
        return userService.updateUser(email, userDetails, result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{email}")
    public UserGetDto makeUserAdmin(@PathVariable String email) {
        logger.info("Promoting user to admin with email: {}", email);
        return userService.makeUserAdmin(email);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{email}")
    public void deleteUser(@PathVariable String email) {
        logger.info("Deleting user with email: {}", email);
        userService.deleteUser(email);
    }

    @GetMapping("/favouriteTeam/{email}")
    public Team getFavouriteTeam(@PathVariable String email) {
        logger.info("Fetching favourite team for user with email: {}", email);
        return userService.getFavouriteTeam(email);
    }

    @GetMapping("/name/{email}")
    public String getUserName(@PathVariable String email) {
        logger.info("Fetching name for user with email: {}", email);
        return userService.getUserName(email);
    }
}
