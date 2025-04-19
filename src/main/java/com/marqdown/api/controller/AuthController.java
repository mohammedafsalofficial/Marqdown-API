package com.marqdown.api.controller;

import com.marqdown.api.dto.LoginRequest;
import com.marqdown.api.dto.SignupRequest;
import com.marqdown.api.dto.AuthResponse;
import com.marqdown.api.exception.MissingAuthorizationHeaderException;
import com.marqdown.api.model.User;
import com.marqdown.api.service.AuthService;
import com.marqdown.api.service.JwtService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, JwtService jwtService) {
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody SignupRequest signupRequest) {
        User savedUser = authService.register(signupRequest);

        AuthResponse.UserDto userDto = AuthResponse.UserDto.builder()
                .fullName(savedUser.getFullName())
                .email(savedUser.getEmail())
                .build();

        AuthResponse response = AuthResponse.builder()
                .success(true)
                .message("User registered successfully.")
                .user(userDto)
                .token(jwtService.generateToken(savedUser.getEmail()))
                .requiresEmailVerification(false)
                .redirectUrl("/")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        User loggedInUser = authService.verifyUser(loginRequest);

        AuthResponse.UserDto userDto = AuthResponse.UserDto.builder()
                .fullName(loggedInUser.getFullName())
                .email(loggedInUser.getEmail())
                .build();

        AuthResponse response = AuthResponse.builder()
                .success(true)
                .message("User logged in successfully.")
                .user(userDto)
                .token(jwtService.generateToken(loggedInUser.getEmail()))
                .requiresEmailVerification(false)
                .redirectUrl("/")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/validate")
    public ResponseEntity<AuthResponse> validateAuthToken(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new MissingAuthorizationHeaderException("Authorization header is missing or malformed");
        }

        String authToken = authHeader.substring(7);
        String email = jwtService.extractUsername(authToken);

        AuthResponse.UserDto userDto = AuthResponse.UserDto.builder()
                .email(email)
                .fullName("")
                .build();

        AuthResponse response = AuthResponse.builder()
                .success(true)
                .message("Token is valid.")
                .user(userDto)
                .token(authToken)
                .requiresEmailVerification(false)
                .redirectUrl("/")
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
