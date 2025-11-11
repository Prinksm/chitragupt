package com.example.demo.Security;

import com.example.demo.dto.LogInRequestDto;
import com.example.demo.dto.LogInResponseDto;
import com.example.demo.dto.SignUpRequestDto;
import com.example.demo.dto.SignUpResponseDto;
import com.example.demo.entity.patientEntity.Patient;
import com.example.demo.entity.userEntity.AuthProviderType;
import com.example.demo.entity.userEntity.Roles;
import com.example.demo.entity.userEntity.User;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.RolesRepository;
import com.example.demo.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;
    private final RolesRepository rolesRepository;

    public LogInResponseDto login(LogInRequestDto loginRequestDto , HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword())
        );

        User user = (User) authentication.getPrincipal();

        String token = authUtil.generateAccessToken(user);
        ResponseCookie cookie= ResponseCookie.from("accessToken",token).httpOnly(false)
                .path("/")
                .maxAge(Duration.ofSeconds(300))
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        String refreshToken = authUtil.generateRefreshToken(user);
        ResponseCookie refreshcookie = ResponseCookie.from("refreshToken",refreshToken).httpOnly(false)
                .path("/")
                .maxAge(Duration.ofSeconds(1200))
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, refreshcookie.toString());

        return new LogInResponseDto(token, refreshToken,user.getId());
    }

    public User signUpInternal(SignUpRequestDto signupRequestDto, AuthProviderType authProviderType, String providerId){
        User user = userRepository.findByEmail(signupRequestDto.getEmail()).orElse(null);
        if(user != null) throw new IllegalArgumentException("User already exists");
        if (signupRequestDto.getPassword()!=null && !signupRequestDto.getPassword().equals(signupRequestDto.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }
        User users = new User();
        users.setProviderId(providerId);
        users.setProviderType(authProviderType);
        users.setFirstName(signupRequestDto.getFirstName());
        users.setMiddleName(signupRequestDto.getMiddleName());
        users.setLastName(signupRequestDto.getLastName());
        users.setEmail(signupRequestDto.getEmail());
        users.setContactNumber(signupRequestDto.getContactNumber());
        if(authProviderType == AuthProviderType.EMAIL){
            users.setPassword(passwordEncoder.encode(signupRequestDto.getPassword()));
        }

        Set<Roles> roles = new HashSet<>();
        Roles patientRole = rolesRepository.findByName("PATIENT").orElseThrow(()-> new RuntimeException("Patient role not found"));
        roles.add(patientRole);

        Roles userRole = rolesRepository.findByName("USER").orElseThrow(()-> new RuntimeException("User role not found"));
        roles.add(userRole);

        users.setRoles(roles);
        users =userRepository.save(users);

        Patient patient = new Patient();
        patient.setUser(users);
        patient.setFirstName(users.getFirstName());
        patient.setMiddleName(users.getMiddleName());
        patient.setLastName(users.getLastName());
        patientRepository.save(patient);
        return users;
    }


    public SignUpResponseDto signup(SignUpRequestDto signupRequestDto) {
        User user = signUpInternal(signupRequestDto, AuthProviderType.EMAIL, null);
        return new SignUpResponseDto(user.getId(), user.getUsername());
    }


    @Transactional
    public ResponseEntity<LogInResponseDto> handleOauth2LoginRequest(OAuth2User oAuth2User, String email, String registrationId , HttpServletResponse response) {
        AuthProviderType providerType = authUtil.getProviderTypeFromRegistrationID(registrationId);
        String providerId = authUtil.determineProviderIdFromOAuth2User(oAuth2User , registrationId);
        String email1 = oAuth2User.getAttribute("email");

        User user = userRepository.findByProviderIdAndProviderType(providerId,providerType).orElse(null);
        User emailUser = userRepository.findByEmail(email1).orElse(null);

        if(user==null && emailUser == null){
            String firstName = oAuth2User.getAttribute("given_name") != null ? oAuth2User.getAttribute("given_name") : oAuth2User.getAttribute("name");
            //String emailAuth = authUtil.determineUsernameFromOAuth2User(oAuth2User , registrationId , providerId);
          signUpInternal(new SignUpRequestDto(firstName,null,oAuth2User.getAttribute("family_name"),null , email1 , null , null ),providerType, providerId);
            user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found after signup"));
            user.setProviderId(providerId);
            user.setProviderType(providerType);
            userRepository.save(user);
            String token = authUtil.generateAccessToken(user);
            String refreshToken = authUtil.generateRefreshToken(user);
            return ResponseEntity.ok(new LogInResponseDto(token,refreshToken, user.getId()));

        }else if(user != null) {
            if(email != null && !email.isBlank() && !email.equals(user.getUsername())) {
                user.setEmail(email);
                userRepository.save(user);
            }
            String token = authUtil.generateAccessToken(user);
            String refreshToken = authUtil.generateRefreshToken(user);
            return ResponseEntity.ok(new LogInResponseDto(token, refreshToken,user.getId()));

        }else if (emailUser != null && emailUser.getPassword() != null && !emailUser.getPassword().isEmpty()) {
            String token = authUtil.generateAccessToken(emailUser);
            String refreshToken = authUtil.generateRefreshToken(emailUser);
            return ResponseEntity.ok(new LogInResponseDto(token, refreshToken,emailUser.getId()));
        } else {
            throw new BadCredentialsException("This email is already registered with provider "+emailUser.getProviderType());
        }

    }
}
