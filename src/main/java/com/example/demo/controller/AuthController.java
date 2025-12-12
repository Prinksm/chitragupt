package com.example.demo.controller;

import com.example.demo.security.AuthService;
import com.example.demo.security.AuthUtil;

import io.jsonwebtoken.Claims;

import com.example.demo.dto.*;
import com.example.demo.entity.userEntity.User;
import com.example.demo.repository.AuthCommon.UserRepository;
import com.example.demo.emails.services.OtpService;
import com.example.demo.emails.services.PasswordResetService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final OtpService otpService;
    private final PasswordResetService passwordResetService;
    private final AuthUtil authUtil;
    private final UserRepository userRepo;

    @PostMapping("/login")
    public ResponseEntity<LogInResponseDto> login(@RequestBody LogInRequestDto loginRequestDto,
            HttpServletResponse response) {
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
    public ResponseEntity<Map<String, String>> verifyOtp(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String otp = request.get("otp");

        otpService.verifyOtp(email, otp);

        Map<String, String> response = Map.of("message", "User verified successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/send-password-link")
    public ResponseEntity<Map<String, String>> sendPasswordLink(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        passwordResetService.sendPasswordResetLink(email);
        Map<String, String> response = Map.of("message", "Password reset link sent successfully");
        return ResponseEntity.ok(response);

    }

    // Forget password & needs password
    @PostMapping("/update-password")
    public ResponseEntity<Map<String, String>> updatePassword(@RequestBody PasswordResetRequestDto request) {
        passwordResetService.updatePasswordWithToken(request.getToken(), request.getPassword(),
                request.getConfirmPassword());
        Map<String, String> response = Map.of("message", "Password updated successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/needs-password")
    public ResponseEntity<Boolean> needsPassword(@RequestParam String email) {
        boolean needsPassword = passwordResetService.needsPassword(email);
        return ResponseEntity.ok(needsPassword);
    }

    @GetMapping("/refresh")
    public ResponseEntity<?> refreshToken(@CookieValue(name = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response) throws Exception {
        if (refreshToken == null || !authUtil.validateToken(refreshToken)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid Refresh Token"));
        }
        try {
            String email = authUtil.getEmailFromToken(refreshToken);
            User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

            String accessToken = authUtil.generateAccessToken(user);
            refreshToken = authUtil.generateRefreshToken(user);

            ResponseCookie accessCookie = ResponseCookie.from("accessToken", accessToken)
                    .httpOnly(true)
                    .path("/")
                    .secure(true)
                    .sameSite("None")
                    .maxAge(Duration.ofMinutes(10))
                    .build();
            ResponseCookie refreshCookie = ResponseCookie.from("refreshToken", refreshToken)
                    .httpOnly(true)
                    .path("/")
                    .secure(true)
                    .sameSite("None")
                    .maxAge(Duration.ofMinutes(20))
                    .build();

            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, accessCookie.toString())
                    .header(HttpHeaders.SET_COOKIE, refreshCookie.toString())
                    .body(Map.of("message", "Tokens refreshed successfully"));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Failed to refresh token"));
        }

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        ResponseCookie clearAccess = ResponseCookie.from("accessToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)
                .build();

        ResponseCookie clearRefresh = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
                .path("/")
                .maxAge(0)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, clearAccess.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, clearRefresh.toString());

        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    @GetMapping("/status")
    public ResponseEntity<?> status(
            @CookieValue(name = "accessToken", required = false) String accessToken) {

        if (accessToken == null) {
            return ResponseEntity.status(401).body(Map.of("authenticated", false));
        }

        // Validate token
        if (!authUtil.validateToken(accessToken) || authUtil.isTokenExpired(accessToken)) {
            return ResponseEntity.status(401).body(Map.of("authenticated", false));
        }

        // Extract user claims
        Claims claims = authUtil.extractClaims(accessToken);

        return ResponseEntity.ok(Map.of(
                "authenticated", true,
                "userId", claims.get("userId"),
                "userName", claims.get("userName"),
                "role", claims.get("role"),
                "email", claims.getSubject()));
    }

}