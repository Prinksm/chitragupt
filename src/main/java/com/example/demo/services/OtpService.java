package com.example.demo.services;

import com.example.demo.entity.userEntity.Otp;
import com.example.demo.entity.userEntity.User;
import com.example.demo.repository.OtpRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OtpService {
    private final UserRepository userRepository;
    private final OtpRepository otpRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public void sendOtp(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));


//        otpRepository.findByUserId(user.getId()).ifPresent(existingOtp -> {
//            otpRepository.delete(existingOtp);
//        });

        String rawOtp = generateOTP();
        String hashedOtp = passwordEncoder.encode(rawOtp);

        Otp otpEntity = new Otp();
        otpEntity.setUser(user);
        otpEntity.setOtp(hashedOtp);
        otpEntity.setExpiresAt(LocalDateTime.now().plusMinutes(5));
        otpEntity=otpRepository.save(otpEntity);

//        System.out.println("Otp to ban chuka h"+otpEntity);
        emailService.sendOtpEmail(user.getEmail(), rawOtp);
//        System.out.println("Email bhej diya h");
    }

    // Verify OTP
    public void verifyOtp(String email, String otp) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Otp otpEntity = otpRepository.findMostRecentByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (otpEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }

        if (!passwordEncoder.matches(otp, otpEntity.getOtp())) {
            throw new RuntimeException("Invalid OTP");
        }

        user.setVerified(true);
        userRepository.save(user);

        otpRepository.delete(otpEntity);
    }

    private String generateOTP() {
        return String.format("%06d", new Random().nextInt(1000000));
    }
}
