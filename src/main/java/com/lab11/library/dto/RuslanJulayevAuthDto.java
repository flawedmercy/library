package com.lab11.library.dto;

import com.lab11.library.entity.RuslanJulayevRole;
import jakarta.validation.constraints.*;
import lombok.*;

public class RuslanJulayevAuthDto {
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class RegisterRequest {
        @NotBlank
        @Size(min = 3, max = 80)
        private String username;

        @NotBlank
        @Email
        private String email;

        @NotBlank
        @Size(min = 6, max = 100)
        private String password;

        private RuslanJulayevRole role;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class LoginRequest {
        @NotBlank
        private String username;

        @NotBlank
        private String password;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class AuthResponse {
        private String token;
        private String tokenType;
        private String username;
        private String role;
    }
}
