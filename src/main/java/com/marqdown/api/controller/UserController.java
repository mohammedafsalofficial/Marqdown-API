package com.marqdown.api.controller;

import com.marqdown.api.dto.UserResponseDto;
import com.marqdown.api.model.User;
import com.marqdown.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/getUser")
    public ResponseEntity<UserResponseDto> getUser(Authentication authentication) {
        String email = authentication.getName();
        User user = userService.getUser(email);
        UserResponseDto userResponseDto = new UserResponseDto(
                user.getFullName(),
                user.getEmail()
        );
        return new ResponseEntity<>(userResponseDto, HttpStatus.OK);
    }
}
