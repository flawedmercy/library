package com.lab11.library.repository;

import com.lab11.library.entity.RuslanJulayevFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RuslanJulayevFileRepository extends JpaRepository<RuslanJulayevFileEntity, Long> {
}
