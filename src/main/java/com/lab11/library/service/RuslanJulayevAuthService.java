package com.lab11.library.service;

import com.lab11.library.dto.RuslanJulayevAuthDto;
import com.lab11.library.entity.*;
import com.lab11.library.exception.DuplicateResourceException;
import com.lab11.library.repository.RuslanJulayevUserRepository;
import com.lab11.library.security.RuslanJulayevJwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RuslanJulayevAuthService {
    private final RuslanJulayevUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final RuslanJulayevJwtUtil jwtUtil;

    @Transactional
    public RuslanJulayevAuthDto.AuthResponse register(RuslanJulayevAuthDto.RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateResourceException("Username already exists: " + request.getUsername());
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + request.getEmail());
        }

        RuslanJulayevRole role = request.getRole() == null ? RuslanJulayevRole.USER : request.getRole();

        RuslanJulayevUser user = RuslanJulayevUser.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        RuslanJulayevUser saved = userRepository.save(user);
        log.info("Registered user id={} username={} role={}", saved.getId(), saved.getUsername(), saved.getRole());

        String token = jwtUtil.generateToken(saved.getUsername(), saved.getRole().name());
        return RuslanJulayevAuthDto.AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .username(saved.getUsername())
                .role(saved.getRole().name())
                .build();
    }

    public RuslanJulayevAuthDto.AuthResponse login(RuslanJulayevAuthDto.LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        RuslanJulayevUser user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid username or password"));

        log.info("User logged in username={}", user.getUsername());

        String token = jwtUtil.generateToken(user.getUsername(), user.getRole().name());
        return RuslanJulayevAuthDto.AuthResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .username(user.getUsername())
                .role(user.getRole().name())
                .build();
    }
}
