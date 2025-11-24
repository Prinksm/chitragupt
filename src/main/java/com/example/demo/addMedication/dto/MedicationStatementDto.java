package com.example.demo.addMedication.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MedicationStatementDto {
    private Long medicationId;
    private String status; // e.g., "ACTIVE"
    private Date effectiveStartDate;
    private Date effectiveEndDate;
    private String notes;
    private DosageRequestDto dosage;
    private TimingDto timing;
}
