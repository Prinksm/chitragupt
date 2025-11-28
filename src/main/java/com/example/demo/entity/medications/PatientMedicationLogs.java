package com.example.demo.entity.medications;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
@Table(name = "patient_medication_logs", schema = "medication")
public class PatientMedicationLogs {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medication_logs_seq")
    @SequenceGenerator(
            name = "medication_logs_seq",
            sequenceName = "medication_logs_id_seq",
            allocationSize = 1
    )
    @Column(name = "medication_logs_id")
    private Long id;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "super_prescription_id", nullable = false)
    private Long superPrescriptionId;

    @Column(name = "prescription_id", nullable = false)
    private Long prescriptionId;

    @Column(name = "statement_id", nullable = false)
    private Long statementId;

    @Column(name = "taken")
    private Boolean taken; // TRUE = completed, FALSE = skipped

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    @Column(name = "dose_time")
    private LocalDateTime doseTime;
    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();
    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
