package com.lab11.library.repository;

import com.lab11.library.entity.RuslanJulayevUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RuslanJulayevUserRepository extends JpaRepository<RuslanJulayevUser, Long> {
    Optional<RuslanJulayevUser> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
