package com.example.demo.entity.patientEntity;

import com.example.demo.entity.codeableConcept.Concepts;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "patient_allergy", schema = "patient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PatientAllergy {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "patient_allergy_id_seq")
    @SequenceGenerator(name = "patient_allergy_id_seq", sequenceName = "patient_allergy_id_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @Column(name = "clinical_status", nullable = false, length = 20)
    private String clinicalStatus;

    @Column(name = "verification_status", length = 20)
    private String verificationStatus;

    @Column(name = "allergy_type", length = 20)
    private String allergyType;

    @Column(length = 50)
    private String category;

    @Column(length = 50)
    private String criticality;

    @Column(name = "allergy_code", nullable = false)
    private Long allergyCode;

    @Column(name = "onset_date")
    private LocalDate onsetDate;

    @Column(name = "recorded_date", nullable = false)
    private LocalDateTime recordedDate;

    @PrePersist
    protected void onCreate() {
        this.recordedDate = LocalDateTime.now();
    }
}
