package com.matchmetrics.security.service.impl;

import com.matchmetrics.security.entity.RegisterRequest;
import com.matchmetrics.security.entity.Role;
import com.matchmetrics.security.entity.User;
import com.matchmetrics.entity.Team;
import com.matchmetrics.security.repository.UserRepository;
import com.matchmetrics.repository.TeamRepository;
import com.matchmetrics.exception.TeamDoesNotExistException;
import com.matchmetrics.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl (UserRepository userRepository, TeamRepository teamRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    public ResponseEntity<?> updateUser(String email, RegisterRequest userDetails, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }

        return userRepository.findByEmail(email).map(user -> {
            if (userDetails.getName() != null && !userDetails.getName().isEmpty()) {
                user.setName(userDetails.getName());
            }

            if (userDetails.getEmail() != null && !userDetails.getEmail().isEmpty()) {
                user.setEmail(userDetails.getEmail());
            }

            if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
                user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
            }

            if (userDetails.getFavouriteTeam() != null && !userDetails.getFavouriteTeam().isEmpty()) {
                Team favouriteTeam = teamRepository.findTeamByName(userDetails.getFavouriteTeam())
                        .orElseThrow(() -> new TeamDoesNotExistException(userDetails.getFavouriteTeam()));
                user.setFavouriteTeam(favouriteTeam);
            }

            User updatedUser = userRepository.save(user);
            return ResponseEntity.ok(updatedUser);
        }).orElse(ResponseEntity.notFound().build());
    }

    public boolean deleteUser(String email) {
        return userRepository.findByEmail(email).map(user -> {
            userRepository.delete(user);
            return true;
        }).orElse(false);
    }

    public Team getFavouriteTeam(String email) {
        return userRepository.findByEmail(email)
                .map(User::getFavouriteTeam)
                .orElse(null);
    }

    public String getUserName(String email) {
        return userRepository.findByEmail(email)
                .map(User::getName)
                .orElse(null);
    }
}
