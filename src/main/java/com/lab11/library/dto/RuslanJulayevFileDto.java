package com.lab11.library.dto;

import lombok.*;

import java.time.LocalDateTime;

public class RuslanJulayevFileDto {
    @Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
    public static class Response {
        private Long id;
        private String originalFileName;
        private String contentType;
        private Long sizeBytes;
        private LocalDateTime uploadedAt;
        private String downloadUrl;
    }
}
