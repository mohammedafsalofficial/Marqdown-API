package com.marqdown.api.service;

import com.marqdown.api.model.User;
import com.marqdown.api.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUser(String email) throws IllegalStateException {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new IllegalStateException("Authenticated user not found in DB: " + email));
    }
}
