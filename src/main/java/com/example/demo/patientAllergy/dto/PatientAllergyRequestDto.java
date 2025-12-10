package com.example.demo.patientAllergy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientAllergyRequestDto {
    private String allergyName;
    private String clinicalStatus;
    private String verificationStatus;
    private String allergyType;
    private String category;
    private String criticality;
    private LocalDate onsetDate;
}
