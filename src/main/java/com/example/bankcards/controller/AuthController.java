package com.example.bankcards.controller;


import com.example.bankcards.dto.JwtResponse;
import com.example.bankcards.dto.LoginRequest;
import com.example.bankcards.dto.MessageResponse;
import com.example.bankcards.dto.SignupRequest;
import com.example.bankcards.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:8080", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signin")
    @ResponseStatus(HttpStatus.CREATED)
    public JwtResponse authenticateUser(@RequestBody LoginRequest loginRequest) {
        return authService.authenticateUser(loginRequest);
    }

    @PostMapping("/signup")
    public MessageResponse registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        return authService.registerUser(signUpRequest);
    }
}
