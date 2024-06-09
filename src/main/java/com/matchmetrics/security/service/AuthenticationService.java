package com.matchmetrics.security.service;

import com.matchmetrics.entity.Team;
import com.matchmetrics.exception.TeamDoesNotExistException;
import com.matchmetrics.repository.TeamRepository;
import com.matchmetrics.security.entity.*;
import com.matchmetrics.security.exception.EmailTakenException;
import com.matchmetrics.security.exception.UserDoesNotExistException;
import com.matchmetrics.security.repository.UserRepository;
import com.matchmetrics.util.BindingResultInspector;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

@Service
public class AuthenticationService {
    private final UserRepository repository;
    private final TeamRepository teamRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final BindingResultInspector bindingResultInspector;

    public AuthenticationService(UserRepository repository,
                                 TeamRepository teamRepository,
                                 PasswordEncoder passwordEncoder,
                                 JwtService jwtService,
                                 AuthenticationManager authenticationManager,
                                 BindingResultInspector bindingResultInspector) {
        this.repository = repository;
        this.teamRepository = teamRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
        this.bindingResultInspector = bindingResultInspector;
    }

    public AuthenticationResponse register(RegisterRequest request,
                                           BindingResult bindingResult) {
        bindingResultInspector.checkBindingResult(bindingResult);

        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailTakenException(request.getEmail());
        }

        Team favouriteTeam = teamRepository.findTeamByName(request.getFavouriteTeam())
                .orElseThrow(() -> new TeamDoesNotExistException(request.getFavouriteTeam()));

        var user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_USER)
                .favouriteTeam(favouriteTeam)
                .build();
        repository.save(user);
        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request,
                                               BindingResult bindingResult) {
        bindingResultInspector.checkBindingResult(bindingResult);

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserDoesNotExistException(request.getEmail()));

        var jwt = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwt)
                .build();
    }
}
