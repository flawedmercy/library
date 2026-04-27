package com.lab11.library.dto;

import jakarta.validation.constraints.*;
import lombok.*;

public class CategoryDto {

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Request {
        @NotBlank(message = "Category name is required")
        @Size(max = 80)
        private String name;

        @Size(max = 300)
        private String description;
    }

    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Long id;
        private String name;
        private String description;
    }
}