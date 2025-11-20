package com.example.demo.PatientContact.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactTelecomDto {
    private Long id;
    private String system;
    private String value;
    private String useCode;
}
