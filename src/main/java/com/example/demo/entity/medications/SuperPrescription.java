package com.example.demo.entity.medications;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "super_prescription", schema = "medication")
public class SuperPrescription {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "super_prescription_generator")
    @SequenceGenerator(
            name = "super_prescription_generator",
            sequenceName = "super_prescription_id_seq",
            schema = "public",
            allocationSize = 1
    )
    @Column(name = "super_prescription_id")
    private Long superPrescriptionId;

    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    @Column(name = "doctor_name")
    private String doctorName;

    @Column(name = "prescription_date", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date prescriptionDate;

    @Column(name = "notes")
    private String notes;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}
