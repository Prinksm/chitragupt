package com.example.demo.dailyMedications.Dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicationWithStatus extends MedicationNormalized {
    private Boolean taken;
    private DoseStatus takenStatus;
    private LocalDateTime doseTime; // scheduled dose datetime
    private LocalDateTime logCreatedAt; // when taken or skipped


    public enum DoseStatus {
        PENDING,
        TAKEN,
        SKIPPED
    }
    public void copyFrom(MedicationNormalized m) {
        this.setDoctorName(m.getDoctorName());
        this.setPrescriptionId(m.getPrescriptionId());
        this.setPrescriptionConditionId(m.getPrescriptionConditionId());
        this.setPrescriptionDate(m.getPrescriptionDate());
        this.setConditionName(m.getConditionName());
        this.setConditionNotes(m.getConditionNotes());
        this.setStatementId(m.getStatementId());
        this.setMedication(m.getMedication());
        this.setMedicationId(m.getMedicationId());
        this.setStatus(m.getStatus());

        this.setEffectiveStartDate(m.getEffectiveStartDate());
        this.setEffectiveEndDate(m.getEffectiveEndDate());
        this.setNotes(m.getNotes());
        this.setDosage(m.getDosage());
        this.setTiming(m.getTiming());
    }
}
