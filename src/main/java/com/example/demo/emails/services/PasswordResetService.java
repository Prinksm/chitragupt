package com.example.demo.emails.services;

import com.example.demo.emails.template.EmailTemplates;
import com.example.demo.entity.userEntity.PasswordResetToken;
import com.example.demo.entity.userEntity.User;
import com.example.demo.repository.AuthCommon.PasswordResetTokenRepository;
import com.example.demo.repository.AuthCommon.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final UserRepository userRepository;
    private  final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final EmailTemplates emailTemplates;
    @Value("${frontendUrl}")
    private String frontendUrl;

    public void sendPasswordResetLink(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not registered"));

        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setUser(user);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        passwordResetTokenRepository.save(resetToken);

        String resetLink = frontendUrl + "/reset-password?token=" + token;;
        emailTemplates.sendResetLink(email, resetLink);
    }


    public void updatePasswordWithToken(String token, String newPassword, String confirmPassword) {
        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Passwords do not match");
        }

        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            passwordResetTokenRepository.delete(resetToken);
            throw new RuntimeException("Token expired");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        user.setVerified(true);
        userRepository.save(user);
        passwordResetTokenRepository.delete(resetToken);
    }


    public boolean needsPassword(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email not registered"));
        return user.getPassword() == null;
    }
}
