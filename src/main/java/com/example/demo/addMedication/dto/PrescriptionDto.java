package com.example.demo.addMedication.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PrescriptionDto {
    private Long patientId;
    private String conditionName;
    private String notes;
    private List<MedicationStatementDto> medications;
}
