package com.matchmetrics.security.service.impl;

import com.matchmetrics.security.entity.*;
import com.matchmetrics.entity.Team;
import com.matchmetrics.security.repository.UserRepository;
import com.matchmetrics.repository.TeamRepository;
import com.matchmetrics.exception.TeamDoesNotExistException;
import com.matchmetrics.security.entity.dto.UserGetDto;
import com.matchmetrics.security.service.UserService;
import com.matchmetrics.util.BindingResultInspector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final PasswordEncoder passwordEncoder;
    private final BindingResultInspector bindingResultInspector;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository, TeamRepository teamRepository, PasswordEncoder passwordEncoder, BindingResultInspector bindingResultInspector) {
        this.userRepository = userRepository;
        this.teamRepository = teamRepository;
        this.passwordEncoder = passwordEncoder;
        this.bindingResultInspector = bindingResultInspector;
    }

    @Override
    public List<UserGetDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new UserGetDto(user.getName(), user.getEmail(), user.getFavouriteTeam().getName(), user.getRole()))
                .collect(Collectors.toList());
    }

    @Override
    public UserGetDto getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> new UserGetDto(user.getName(), user.getEmail(), user.getFavouriteTeam().getName(), user.getRole()))
                .orElseGet(() -> {
                    logger.error("User with email {} not found", email);
                    return null;
                });
    }

    @Override
    public UserGetDto updateUser(String email, RegisterRequest userDetails, BindingResult result) {
        bindingResultInspector.checkBindingResult(result);

        return userRepository.findByEmail(email).map(user -> {
            if (userDetails.getName() != null && !userDetails.getName().isEmpty()) {
                user.setName(userDetails.getName());
            }

            if (userDetails.getEmail() != null && !userDetails.getEmail().isEmpty()) {
                // TODO: Check if email is user's email
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
            return new UserGetDto(updatedUser.getName(), updatedUser.getEmail(), updatedUser.getFavouriteTeam().getName(), updatedUser.getRole());
        }).orElseGet(() -> {
            logger.error("User with email {} not found", email);
            return null;
        });
    }

    @Override
    public void deleteUser(String email) {
        userRepository.findByEmail(email).ifPresentOrElse(
                userRepository::delete,
                () -> logger.error("User with email {} not found", email)
        );
    }

    @Override
    public Team getFavouriteTeam(String email) {
        return userRepository.findByEmail(email)
                .map(User::getFavouriteTeam)
                .orElseGet(() -> {
                    logger.error("User with email {} not found", email);
                    return null;
                });
    }

    @Override
    public String getUserName(String email) {
        return userRepository.findByEmail(email)
                .map(User::getName)
                .orElseGet(() -> {
                    logger.error("User with email {} not found", email);
                    return null;
                });
    }

    @Override
    public UserGetDto makeUserAdmin(String email) {
        return userRepository.findByEmail(email).map(user -> {
            user.setRole(Role.ROLE_ADMIN);
            User updatedUser = userRepository.save(user);
            return new UserGetDto(updatedUser.getName(), updatedUser.getEmail(), updatedUser.getFavouriteTeam().getName(), updatedUser.getRole());
        }).orElseGet(() -> {
            logger.error("User with email {} not found", email);
            return null;
        });
    }
}
