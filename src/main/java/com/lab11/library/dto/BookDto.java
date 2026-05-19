package com.lab11.library.dto;

import jakarta.validation.constraints.*;
import lombok.*;


public class BookDto {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {
        @NotBlank(message = "Title is required")
        @Size(max = 200)
        private String title;

        @Size(max = 20, message = "ISBN must be at most 20 characters")
        private String isbn;

        @Min(value = 1000, message = "Published year seems too old")
        @Max(value = 2100, message = "Published year seems too far in the future")
        private Integer publishedYear;

        @NotNull(message = "Author ID is required")
        private Long authorId;

        private Long categoryId;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Long id;
        private String title;
        private String isbn;
        private Integer publishedYear;
        private String authorName;
        private String categoryName;
    }
}