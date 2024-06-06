package com.matchmetrics.security.controller;

import com.matchmetrics.security.entity.RegisterRequest;
import com.matchmetrics.entity.Team;
import com.matchmetrics.security.entity.dto.UserGetDto;
import com.matchmetrics.security.service.UserService;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("matchmetrics/api/v0/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public List<UserGetDto> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{email}")
    public UserGetDto getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);;
    }

    @PutMapping("/update/{email}")
    public UserGetDto updateUser(
            @PathVariable String email,
            @RequestBody RegisterRequest userDetails,
            BindingResult result) {
        return userService.updateUser(email, userDetails, result);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{email}")
    public UserGetDto makeUserAdmin(@PathVariable String email) {
        return userService.makeUserAdmin(email);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{email}")
    public void deleteUser(@PathVariable String email) {
        userService.deleteUser(email);
    }

    @GetMapping("/favouriteTeam/{email}")
    public Team getFavouriteTeam(@PathVariable String email) {
        return userService.getFavouriteTeam(email);
    }

    @GetMapping("/name/{email}")
    public String getUserName(@PathVariable String email) {
        return userService.getUserName(email);
    }
}
