package com.example.demo.entity.patientEntity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "patient_contact", schema = "patient")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PatientContact {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient_contact_id_seq")
    @SequenceGenerator(name = "patient_contact_id_seq", sequenceName = "patient_contact_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "relationship_type", length = 50)
    private String relationshipType;

    @Column(name = "firstName", nullable = false, length = 30)
    private String firstName;

    @Column(name = "middleName", length = 30)
    private String middleName;

    @Column(name = "lastName", length = 30)
    private String lastName;

    @Column(name = "period_start")
    private LocalDate periodStart;

    @Column(name = "period_end")
    private LocalDate periodEnd;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ContactAddress> contactAddresses;

    @OneToMany(mappedBy = "contact", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ContactTelecom> contactTelecoms;

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
