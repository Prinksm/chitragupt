package com.example.demo.services;

import com.example.demo.entity.userEntity.Otp;
import com.example.demo.entity.userEntity.PasswordResetToken;
import com.example.demo.entity.userEntity.User;
import com.example.demo.repository.OtpRepository;
import com.example.demo.repository.PasswordResetTokenRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CleanupService {
    private final OtpRepository otpRepository;
    private final UserRepository userRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;



    @Scheduled(fixedRate = 360000) // Run every 6 minutes
    public void cleanupExpiredOtps() {
        LocalDateTime now = LocalDateTime.now();
        List<Otp> expiredOtps = otpRepository.findByExpiresAtBefore(now);
        otpRepository.deleteAll(expiredOtps);
    }

    @Scheduled(fixedRate = 600000) // Run every 10 minutes
    public void cleanupUnverifiedUsers() {
        LocalDateTime tenMinutesAgo = LocalDateTime.now().minusMinutes(10);
        List<User> unverifiedUsers = userRepository.findByVerifiedFalseAndCreatedAtBefore(tenMinutesAgo);
        userRepository.deleteAll(unverifiedUsers);
    }

    @Scheduled(fixedRate = 720000) // Run every 12 minutes
    public void cleanupExpiredPasswordResetTokens() {
        LocalDateTime now = LocalDateTime.now();
        List<PasswordResetToken> expiredTokens = passwordResetTokenRepository.findByExpiryDateBefore(now);
        passwordResetTokenRepository.deleteAll(expiredTokens);
    }
}
