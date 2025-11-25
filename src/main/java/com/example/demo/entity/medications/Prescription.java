package com.example.demo.entity.medications;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "prescription", schema = "medication")
public class Prescription {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prescription_generator")
    @SequenceGenerator(
            name = "prescription_generator",
            sequenceName = "prescription_id_seq",
            schema = "public",
            allocationSize = 1
    )
    @Column(name = "prescription_id")
    private Long prescriptionId;

    @Column(name = "super_prescription_id", nullable = false)
    private Long superPrescriptionId;

    @Column(name = "reason_id", nullable = false)
    private Long reasonId;

    @Column(name = "notes")
    private String notes;
}
