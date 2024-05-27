package com.matchmetrics.security.service.impl;

import com.matchmetrics.security.entity.User;
import com.matchmetrics.entity.Team;
import com.matchmetrics.security.repository.UserRepository;
import com.matchmetrics.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public ResponseEntity<?> createUser(User user, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        User createdUser = userRepository.save(user);
        return ResponseEntity.ok(createdUser);
    }

    @Override
    public ResponseEntity<?> updateUser(String email, User userDetails, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(result.getAllErrors());
        }
        return userRepository.findByEmail(email).map(user -> {
            user.setName(userDetails.getName());
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword());
            user.setRole(userDetails.getRole());
            user.setFavouriteTeam(userDetails.getFavouriteTeam());
            User updatedUser = userRepository.save(user);
            return ResponseEntity.ok(updatedUser);
        }).orElse(ResponseEntity.notFound().build());
    }

    @Override
    public boolean deleteUser(String email) {
        return userRepository.findByEmail(email).map(user -> {
            userRepository.delete(user);
            return true;
        }).orElse(false);
    }

    @Override
    public Team getFavouriteTeam(String email) {
        return userRepository.findByEmail(email).map(User::getFavouriteTeam).orElse(null);
    }

    @Override
    public String getUserName(String email) {
        return userRepository.findByEmail(email).map(User::getName).orElse(null);
    }
}
