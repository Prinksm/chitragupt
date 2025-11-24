package com.example.demo.entity.patientEntity;

import com.example.demo.entity.userEntity.CommonTelecom;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "contact_telecom", schema = "patient")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactTelecom {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "contact_telecom_id_seq")
    @SequenceGenerator(name = "contact_telecom_id_seq", sequenceName = "contact_telecom_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contact_id", nullable = false)
    private PatientContact contact;

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
