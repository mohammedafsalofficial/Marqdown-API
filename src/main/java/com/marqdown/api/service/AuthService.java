package com.marqdown.api.service;

import com.marqdown.api.dto.LoginRequest;
import com.marqdown.api.dto.SignupRequest;
import com.marqdown.api.exception.EmailAlreadyExistsException;
import com.marqdown.api.model.AuthenticationPrincipal;
import com.marqdown.api.model.User;
import com.marqdown.api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public User register(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new EmailAlreadyExistsException("Email already taken.");
        }

        User user = User.builder()
                .id(0L)
                .fullName(signupRequest.getFullName())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .build();
        User savedUser = userRepository.save(user);

        logger.info("New user registered with email: {}", signupRequest.getEmail());
        return savedUser;
    }

    public User verifyUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));

        AuthenticationPrincipal authPrincipal = (AuthenticationPrincipal) authentication.getPrincipal();
        logger.info("User logged in with email: {}", loginRequest.getEmail());
        return authPrincipal.getUser();
    }
}
