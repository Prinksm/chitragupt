package com.example.demo.controller;

import com.example.demo.Security.AuthService;
import com.example.demo.dto.LogInRequestDto;
import com.example.demo.dto.LogInResponseDto;
import com.example.demo.dto.SignUpRequestDto;
import com.example.demo.dto.SignUpResponseDto;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LogInResponseDto> login(@RequestBody LogInRequestDto loginRequestDto , HttpServletResponse response) {
        return ResponseEntity.ok(authService.login(loginRequestDto , response));
    }

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponseDto> signup(@RequestBody SignUpRequestDto signupRequestDto) {
        return ResponseEntity.ok(authService.signup(signupRequestDto));
    }
}
