package com.matchmetrics.security.controller;

import com.matchmetrics.security.entity.AuthenticationRequest;
import com.matchmetrics.security.entity.AuthenticationResponse;
import com.matchmetrics.security.entity.RegisterRequest;
import com.matchmetrics.security.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:63342")
@RestController
@RequestMapping("matchmetrics/api/v0/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request,
            BindingResult bindingResult
    ) {
        return ResponseEntity.ok(authenticationService.register(request, bindingResult));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @Valid @RequestBody AuthenticationRequest request,
            BindingResult bindingResult
    ) {
        return ResponseEntity.ok(authenticationService.authenticate(request, bindingResult));
    }
}
