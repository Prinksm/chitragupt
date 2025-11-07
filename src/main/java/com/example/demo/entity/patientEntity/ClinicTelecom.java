package com.example.demo.entity.patientEntity;

import com.example.demo.entity.userEntity.CommonTelecom;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "clinic_telecom", schema = "patient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClinicTelecom {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clinic_telecom_id_seq")
    @SequenceGenerator(name = "clinic_telecom_id_seq", sequenceName = "clinic_telecom_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;

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
