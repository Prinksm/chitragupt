package com.example.demo.patientAllergy.repository;

import com.example.demo.entity.patientEntity.PatientAllergy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientAllergyRepository extends JpaRepository<PatientAllergy, Long> {
    List<PatientAllergy> findByPatientId(Long patientId);
    List<PatientAllergy>findAllByPatientId(Long patientId);
}
