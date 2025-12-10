package com.example.demo.patientAllergy.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientAllergyResponseDto {
    private Long id;
    private Long patientId;
    private Long conceptId;
    private String allergyName;
    private String clinicalStatus;
    private String verificationStatus;
    private String allergyType;
    private String category;
    private String criticality;
    private LocalDate onsetDate;
    private LocalDateTime recordedDate;
}
