package com.marqdown.api.exception;

import com.marqdown.api.dto.AuthResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private ResponseEntity<AuthResponse> buildErrorResponse(String message, HttpStatus status) {
        AuthResponse response = AuthResponse.builder()
                .success(false)
                .message(message)
                .build();
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<AuthResponse> handleEmailAlreadyExists(EmailAlreadyExistsException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<AuthResponse> handleValidationException(MethodArgumentNotValidException e) {
        List<AuthResponse.ValidationError> validationErrors = new ArrayList<>();

        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            validationErrors.add(AuthResponse.ValidationError.builder()
                            .field(fieldError.getField())
                            .message(fieldError.getDefaultMessage())
                    .build());
        });

        AuthResponse response = AuthResponse.builder()
                .success(false)
                .message("Validation failed.")
                .errors(validationErrors)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<AuthResponse> handleAuthenticationException(AuthenticationException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtTokenExpiredException.class)
    public ResponseEntity<AuthResponse> handleTokenExpiration(JwtTokenExpiredException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(JwtTokenException.class)
    public ResponseEntity<AuthResponse> handleJwtTokenException(JwtTokenException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(MissingAuthorizationHeaderException.class)
    public ResponseEntity<AuthResponse> handleMissingAuthorizationHeader(MissingAuthorizationHeaderException e) {
        return buildErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalState(IllegalStateException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
