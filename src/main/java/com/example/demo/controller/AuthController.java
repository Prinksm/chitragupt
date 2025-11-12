package com.example.demo.controller;

import com.example.demo.Security.AuthService;
import com.example.demo.dto.*;
import com.example.demo.services.OtpService;
import com.example.demo.services.PasswordResetService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final OtpService otpService;
    private final PasswordResetService passwordResetService;


    @PostMapping("/login")
    public ResponseEntity<LogInResponseDto> login(@RequestBody LogInRequestDto loginRequestDto, HttpServletResponse response) {
        return ResponseEntity.ok(authService.login(loginRequestDto, response));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signup(@RequestBody SignUpRequestDto signupRequestDto) {
        return ResponseEntity.ok(authService.signup(signupRequestDto));
    }


    @PostMapping("/send-otp")
    public ResponseEntity<String> sendOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        System.out.println("Inside controller" + email);
        otpService.sendOtp(email);
        System.out.println("after controller");
        return ResponseEntity.ok("OTP sent successfully");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");
        otpService.verifyOtp(email, otp);
        return ResponseEntity.ok("User verified successfully");
    }

    @PostMapping("/send-password-link")
    public ResponseEntity<String> sendPasswordLink(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        passwordResetService.sendPasswordResetLink(email);
        return ResponseEntity.ok("Password reset link sent successfully");
    }

    @PostMapping("/update-password")
    public ResponseEntity<String> updatePassword(@RequestBody PasswordResetRequestDto request) {
        passwordResetService.updatePasswordWithToken(request.getToken(), request.getPassword(), request.getConfirmPassword());
        return ResponseEntity.ok("Password updated successfully");
    }

    @GetMapping("/needs-password")
    public ResponseEntity<Boolean> needsPassword(@RequestParam String email) {
        boolean needsPassword = passwordResetService.needsPassword(email);
        return ResponseEntity.ok(needsPassword);
    }
}