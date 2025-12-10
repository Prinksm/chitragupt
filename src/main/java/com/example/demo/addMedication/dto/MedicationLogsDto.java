package com.example.demo.addMedication.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MedicationLogsDto {
    private Long patientId;
    private Long superPrescriptionId;
    private Long prescriptionId;
    private Long statementId;
    private Boolean taken;
    private LocalDateTime doseTime; // frontend will send this


}
