package com.example.demo.addMedication.dto;

import lombok.*;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MedicationStatementDto {
    private Long medicationId;
    private String status; // e.g., "ACTIVE"
    private LocalDateTime effectiveStartDate;
    private LocalDateTime effectiveEndDate;
    private String notes;
    private DosageRequestDto dosage;
    private TimingDto timing;
}
