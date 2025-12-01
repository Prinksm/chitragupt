package com.example.demo.addMedication.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MedicationResponseDto {
    private Long statementId;
    private String medication;
    private Long medicationId;
    private String status; // e.g., "ACTIVE"
    private Date effectiveStartDate;
    private Date effectiveEndDate;
    private String notes;
    private DosageResponseDto dosage;
    private TimingResponseDto timing;
}
