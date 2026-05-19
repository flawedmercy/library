package com.lab11.library.controller;

import com.lab11.library.dto.RuslanJulayevAuthDto;
import com.lab11.library.service.RuslanJulayevAuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RuslanJulayevAuthController {
    private final RuslanJulayevAuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RuslanJulayevAuthDto.AuthResponse> register(
            @Valid @RequestBody RuslanJulayevAuthDto.RegisterRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<RuslanJulayevAuthDto.AuthResponse> login(
            @Valid @RequestBody RuslanJulayevAuthDto.LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
