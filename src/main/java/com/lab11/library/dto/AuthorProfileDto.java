package com.lab11.library.dto;

import jakarta.validation.constraints.*;
import lombok.*;

public class AuthorProfileDto {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {
        @Size(max = 500, message = "Bio must be at most 500 characters")
        private String bio;

        @Size(max = 100, message = "Nationality must be at most 100 characters")
        private String nationality;

        @Min(value = 1000, message = "Birth year seems invalid")
        @Max(value = 2100, message = "Birth year seems invalid")
        private Integer birthYear;

        @NotNull(message = "Author ID is required")
        private Long authorId;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Long id;
        private String bio;
        private String nationality;
        private Integer birthYear;
        private Long authorId;
        private String authorName;
    }
}