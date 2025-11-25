package com.example.demo.addMedication.dto;

import lombok.*;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter


public class SuperPrescriptionResponseDto {
    private Long patientId;
    private String doctorName;
    private Date prescriptionDate;
    private String notes;
    private List<PrescriptionResponseDto> prescriptions;
}