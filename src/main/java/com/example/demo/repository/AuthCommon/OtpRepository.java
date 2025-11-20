package com.example.demo.repository.AuthCommon;

import com.example.demo.entity.userEntity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    @Query("SELECT o FROM Otp o WHERE o.user.id = :userId ORDER BY o.createdAt DESC LIMIT 1")
    Optional<Otp> findMostRecentByUserId(@Param("userId") Long userId);

    List<Otp> findByExpiresAtBefore(LocalDateTime threshold);

    Optional<Object> findByUserId(Long id);
}
