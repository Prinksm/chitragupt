package com.example.demo.addMedication.repo;

import com.example.demo.entity.medications.Prescription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrescriptionRepo extends JpaRepository<Prescription,Long> {
    List<Prescription> findByPatientId(Long patientId);
}
