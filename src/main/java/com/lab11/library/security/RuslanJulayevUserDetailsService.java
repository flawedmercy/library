package com.lab11.library.security;

import com.lab11.library.entity.RuslanJulayevUser;
import com.lab11.library.repository.RuslanJulayevUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RuslanJulayevUserDetailsService implements UserDetailsService {
    private final RuslanJulayevUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        RuslanJulayevUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
