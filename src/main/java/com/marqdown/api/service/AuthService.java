package com.marqdown.api.service;

import com.marqdown.api.dto.SignupRequest;
import com.marqdown.api.exception.EmailAlreadyExistsException;
import com.marqdown.api.model.User;
import com.marqdown.api.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
}
