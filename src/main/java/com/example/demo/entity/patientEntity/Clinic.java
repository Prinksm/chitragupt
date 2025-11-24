package com.example.demo.entity.patientEntity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "clinic", schema = "patient")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Clinic {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clinic_id_seq")
    @SequenceGenerator(name = "clinic_id_seq", sequenceName = "clinic_id_seq", allocationSize = 1)
    private Long id;

    @Column(name = "clinicName", nullable = false, length = 30)
    private String clinicName;

    @Column(name = "clinicEmail", length = 30)
    private String clinicEmail;

    @Column(name = "clinicAddress", length = 100)
    private String clinicAddress;

    @OneToMany(mappedBy = "clinic", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ClinicTelecom> clinicTelecoms;
}
