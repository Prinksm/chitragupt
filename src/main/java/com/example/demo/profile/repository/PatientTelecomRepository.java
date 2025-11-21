package com.example.demo.profile.repository;

import com.example.demo.entity.patientEntity.PatientTelecom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PatientTelecomRepository extends JpaRepository<PatientTelecom,Long> {
}
