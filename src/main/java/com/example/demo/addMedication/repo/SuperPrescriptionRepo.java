package com.example.demo.addMedication.repo;

import com.example.demo.entity.medications.Prescription;
import com.example.demo.entity.medications.SuperPrescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SuperPrescriptionRepo extends JpaRepository<SuperPrescription,Long> {
    List<SuperPrescription> findByPatientId(Long patientId);
    boolean existsByPatientId(Long patientId);
}
