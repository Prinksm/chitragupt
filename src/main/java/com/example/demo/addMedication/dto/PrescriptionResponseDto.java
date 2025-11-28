package com.example.demo.addMedication.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PrescriptionResponseDto {
    private Long prescriptionId;
    private String conditionName;
    private String notes;
    private List<MedicationResponseDto> medications;
}
