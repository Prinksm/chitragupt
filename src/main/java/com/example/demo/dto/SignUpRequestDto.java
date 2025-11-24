package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {
    private String firstName;
    private String middleName;
    private String lastName;
    private String contactNumber;
    private String email;
    private String password;
    private String confirmPassword;
}
