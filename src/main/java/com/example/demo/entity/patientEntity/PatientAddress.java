package com.example.demo.entity.patientEntity;

import com.example.demo.entity.userEntity.CommonAddress;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_address", schema = "patient")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient_address_id_seq")
    @SequenceGenerator(name = "patient_address_id_seq", sequenceName = "patient_address_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id", nullable = false)
    private CommonAddress address;

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