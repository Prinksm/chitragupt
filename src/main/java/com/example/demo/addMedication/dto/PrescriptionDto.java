package com.example.demo.addMedication.dto;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PrescriptionDto {
    private String conditionName;
    private String notes;
    private List<MedicationStatementDto> medications;
}
