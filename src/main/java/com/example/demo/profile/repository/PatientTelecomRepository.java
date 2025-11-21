package com.example.demo.profile.repository;

import com.example.demo.entity.patientEntity.PatientTelecom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PatientTelecomRepository extends JpaRepository<PatientTelecom,Long> {
    @Query("SELECT pt FROM PatientTelecom pt WHERE pt.id = :telecomId AND pt.patient.id = :patientId")
    Optional<PatientTelecom> findByIdAndPatientId(@Param("telecomId") Long telecomId, @Param("patientId") Long patientId);

    @Query("SELECT COUNT(pt) FROM PatientTelecom pt WHERE pt.patient.id = :patientId")
    long countByPatientId(@Param("patientId") Long patientId);

    boolean existsByTelecomId(Long telecomId);
}
