package com.example.demo.Security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.util.Arrays;
import java.util.List;


@Configuration
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final OAuth2SuccessHandler oAuth2SuccessHandler;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors(cors->corsConfigurationSource())
//                .cors(c -> c.configurationSource(customCorsConfiguration))
                .csrf(csrfConfig -> csrfConfig.disable())
                .sessionManagement(sessionConfig ->
                        sessionConfig.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/public/**", "/auth/**","/data-extractor/**").permitAll()
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/patient/**").hasAnyRole("ADMIN" , "PATIENT" , "USER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login(oAuth2 ->oAuth2.failureHandler(
                (request, response, exception) -> {
                    log.error("oAuth2 error :{}",exception.getMessage());
                    handlerExceptionResolver.resolveException(request, response, null, exception);

                }).successHandler(oAuth2SuccessHandler)
        )
                .exceptionHandling(exceptionHandlingConfigurer ->
                exceptionHandlingConfigurer.accessDeniedHandler((request, response, accessDeniedException) -> {
                    handlerExceptionResolver.resolveException(request, response, null, accessDeniedException);
                }));


        return httpSecurity.build();
    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(List.of("http://localhost:8089", "http://localhost:4200"));
        configuration.setAllowedMethods(List.of("GET","POST","PUT", "DELETE","OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization","Cache-Control","Content-Type"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Set-Cookie"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**",configuration);

        return source;
    }
}
