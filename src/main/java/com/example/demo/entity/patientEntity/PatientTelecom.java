package com.example.demo.entity.patientEntity;

import com.example.demo.entity.userEntity.CommonTelecom;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_telecom", schema = "patient")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientTelecom {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient_telecom_id_seq")
    @SequenceGenerator(name = "patient_telecom_id_seq", sequenceName = "patient.patient_telecom_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "telecom_id", nullable = false)
    private CommonTelecom telecom;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
