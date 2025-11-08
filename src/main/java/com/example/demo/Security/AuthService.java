package com.example.demo.Security;

import com.example.demo.dto.LogInRequestDto;
import com.example.demo.dto.LogInResponseDto;
import com.example.demo.dto.SignUpRequestDto;
import com.example.demo.dto.SignUpResponseDto;
import com.example.demo.entity.patientEntity.Patient;
import com.example.demo.entity.userEntity.User;
import com.example.demo.repository.PatientRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;
    private final PatientRepository patientRepository;
    private final PasswordEncoder passwordEncoder;

    public LogInResponseDto login(LogInRequestDto loginRequestDto) {
//        Authentication authentication = authenticationManager.authenticate(
//                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword())
//        );

//        User user = (User) authentication.getPrincipal();
        User user = userRepository.findByEmail(loginRequestDto.getEmail()).orElseThrow(RuntimeException::new);

        if(user.getPassword().equals(loginRequestDto.getPassword())){
            String token = authUtil.generateAccessToken(user);
            return new LogInResponseDto(token, user.getId());
        }
        return null;
    }

    public SignUpResponseDto signup(SignUpRequestDto signupRequestDto) {
        User user = userRepository.findByEmail(signupRequestDto.getEmail()).orElse(null);
        if(user != null) throw new IllegalArgumentException("User already exists");
        if (!signupRequestDto.getPassword().equals(signupRequestDto.getConfirmPassword())) {
            throw new RuntimeException("Passwords do not match");
        }
        User users = new User();
        users.setFirstName(signupRequestDto.getFirstName());
        users.setMiddleName(signupRequestDto.getMiddleName());
        users.setLastName(signupRequestDto.getLastName());
        users.setEmail(signupRequestDto.getEmail());
        users.setContactNumber(signupRequestDto.getContactNumber());
        users.setPassword("svisrb");

        users =userRepository.save(users);

        Patient patient = new Patient();
        patient.setUserId(users.getId());
        patient.setFirstName(users.getFirstName());
        patient.setMiddleName(users.getMiddleName());
        patient.setLastName(users.getLastName());
        patientRepository.save(patient);
        return new SignUpResponseDto(users.getId(), users.getEmail());
    }
}
