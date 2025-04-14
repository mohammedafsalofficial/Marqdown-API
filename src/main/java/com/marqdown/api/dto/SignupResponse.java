package com.marqdown.api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
@Builder
public class SignupResponse {

    private boolean success;
    private String message;
    private UserDto user;
    private List<ValidationError> errors;
    private String token;
    private boolean requiresEmailVerification;
    private String redirectUrl;
    private Map<String, Object> metadata;

    @Data
    @Builder
    public static class UserDto {
        private String fullName;
        private String email;
    }

    @Data
    @Builder
    public static class ValidationError {
        private String field;
        private String message;
    }
}
