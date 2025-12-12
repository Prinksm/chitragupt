package com.example.demo.security;

import com.example.demo.dto.LogInResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler implements AuthenticationSuccessHandler {
        private final AuthService authService;
        private final ObjectMapper objectMapper;
        private final AuthUtil authUtil;
        @Value("${jwt.accessTokenValidity}")
        private Long accessTokenValidity;
        @Value("${jwt.refreshTokenValidity}")
        private Long refreshTokenValidity;
        @Value("${frontendUrl}")
        private String frontendUrl;

        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                        Authentication authentication) throws IOException, ServletException {

                OAuth2AuthenticationToken token = (OAuth2AuthenticationToken) authentication;
                OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
                String email = oAuth2User.getAttribute("email");
                String registrationId = token.getAuthorizedClientRegistrationId();
                ResponseEntity<LogInResponseDto> loginResponse = authService.handleOauth2LoginRequest(oAuth2User, email,
                                registrationId, response);
                // ResponseEntity<LogInResponseDto> loginResponse =
                // authService.handleOauth2LoginRequest(oAuth2User , email , registrationId,
                // response);
                String accessToken = loginResponse.getBody().getJwt();
                ResponseCookie cookie = ResponseCookie.from("accessToken", accessToken).httpOnly(true)
                                .path("/")
                                .maxAge(Duration.ofSeconds(accessTokenValidity))
                                .secure(true)
                                .sameSite("None")
                                .build();
                response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

                String refreshToken = loginResponse.getBody().getRefreshToken();
                ResponseCookie refreshcookie = ResponseCookie.from("refreshToken", refreshToken).httpOnly(true)
                                .path("/")
                                .maxAge(Duration.ofSeconds(refreshTokenValidity))
                                .secure(true)
                                .sameSite("None")
                                .build();

                response.addHeader(HttpHeaders.SET_COOKIE, refreshcookie.toString());
                response.sendRedirect(frontendUrl + "/user");
                // response.setStatus(loginResponse.getStatusCode().value());
                // response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                // response.getWriter().write(objectMapper.writeValueAsString(loginResponse.getBody()));
        }
}
