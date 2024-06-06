package com.matchmetrics.security.controller;

import com.matchmetrics.security.entity.MakeUserAdminResponse;
import com.matchmetrics.security.entity.RegisterRequest;
import com.matchmetrics.security.entity.UpdateUserResponse;
import com.matchmetrics.security.entity.User;
import com.matchmetrics.entity.Team;
import com.matchmetrics.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("matchmetrics/api/v0/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/all")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        User user = userService.getUserByEmail(email);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update/{email}")
    public ResponseEntity<UpdateUserResponse> updateUser(
            @PathVariable String email,
            @RequestBody RegisterRequest userDetails,
            BindingResult result) {
        UpdateUserResponse response = userService.updateUser(email, userDetails, result);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/admin/{email}")
    public ResponseEntity<MakeUserAdminResponse> makeUserAdmin(@PathVariable String email) {
        MakeUserAdminResponse response = userService.makeUserAdmin(email);
        if (response.isSuccess()) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/delete/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable String email) {
        boolean deleted = userService.deleteUser(email);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/favouriteTeam/{email}")
    public ResponseEntity<Team> getFavouriteTeam(@PathVariable String email) {
        Team favouriteTeam = userService.getFavouriteTeam(email);
        if (favouriteTeam != null) {
            return ResponseEntity.ok(favouriteTeam);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/name/{email}")
    public ResponseEntity<String> getUserName(@PathVariable String email) {
        String name = userService.getUserName(email);
        if (name != null) {
            return ResponseEntity.ok(name);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
