package com.lab11.library.dto;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class RuslanJulayevAsyncDto {
    private String processName;
    private Long result;
    private String message;
}
