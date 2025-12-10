package com.example.demo.sharePrescription.repository;

import com.example.demo.entity.medications.ShareToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ShareTokenRepository extends JpaRepository<ShareToken, Long> {

    Optional<ShareToken> findByToken(String token);

    void deleteByExpiresAtBefore(LocalDateTime time);
}
