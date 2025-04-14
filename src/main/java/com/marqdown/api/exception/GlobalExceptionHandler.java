package com.marqdown.api.exception;

import com.marqdown.api.dto.SignupResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<SignupResponse> handleEmailAlreadyExists(EmailAlreadyExistsException e) {
        SignupResponse response = SignupResponse.builder()
                .success(false)
                .message(e.getMessage())
                .build();
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<SignupResponse> handleValidationException(MethodArgumentNotValidException e) {
        List<SignupResponse.ValidationError> validationErrors = new ArrayList<>();

        e.getBindingResult().getFieldErrors().forEach(fieldError -> {
            validationErrors.add(SignupResponse.ValidationError.builder()
                            .field(fieldError.getField())
                            .message(fieldError.getDefaultMessage())
                    .build());
        });

        SignupResponse response = SignupResponse.builder()
                .success(false)
                .message("Validation failed.")
                .errors(validationErrors)
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
