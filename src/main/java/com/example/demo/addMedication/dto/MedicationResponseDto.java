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
    private String medication;
    private String status; // e.g., "ACTIVE"
    private Date effectiveStartDate;
    private Date effectiveEndDate;
    private String notes;
    private DosageResponseDto dosage;
    private TimingDto timing;
}
