package com.example.demo.repository;
import com.example.demo.entity.userEntity.CommonTelecom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommonTelecomRepository extends JpaRepository<CommonTelecom, Long> {
    Optional<CommonTelecom> findById(Long id);

    // Custom query to find telecoms by patient ID
    @Query("SELECT ct FROM CommonTelecom ct JOIN PatientTelecom pt ON ct.id = pt.telecom.Id WHERE pt.patient.Id = :patientId")
    List<CommonTelecom> findByPatientId(@Param("patientId") Long patientId);
}

