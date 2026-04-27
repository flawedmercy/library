package com.lab11.library.dto;

import jakarta.validation.constraints.*;
import lombok.*;

public class ReviewDto {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {
        @NotBlank(message = "Reviewer name is required")
        @Size(max = 100)
        private String reviewerName;

        @NotBlank(message = "Content is required")
        @Size(min = 10, max = 1000)
        private String content;

        @NotNull
        @Min(value = 1, message = "Rating must be between 1 and 5")
        @Max(value = 5, message = "Rating must be between 1 and 5")
        private Integer rating;

        @NotNull(message = "Book ID is required")
        private Long bookId;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Long id;
        private String reviewerName;
        private String content;
        private Integer rating;
        private Long bookId;
        private String bookTitle;
    }
}