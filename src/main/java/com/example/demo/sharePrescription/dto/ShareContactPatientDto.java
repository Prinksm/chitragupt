package com.example.demo.sharePrescription.dto;

import com.example.demo.addMedication.dto.SuperPrescriptionResponseDto;
import lombok.Data;

import java.util.List;
@Data
public class ShareContactPatientDto {
    private String shareToken;

//    private Long patientId;
    private String firstName;
    private String middleName;
    private String lastName;

    private String relationshipType;

    private List<SuperPrescriptionResponseDto> prescriptions;
}
