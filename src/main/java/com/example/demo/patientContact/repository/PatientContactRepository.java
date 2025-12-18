package com.example.demo.patientContact.repository;

import com.example.demo.entity.patientEntity.PatientContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PatientContactRepository extends JpaRepository<PatientContact, Long> {
    List<PatientContact> findAllByPatientId(long id);
    Optional<PatientContact> findByPatientId(Long id);
    @Query(nativeQuery = true, value = "SELECT * FROM patient.patient_contact WHERE patient_id = :patient_id")
    List<PatientContact> findByPatient_Id(@Param("patient_id") Long patient_id);

    boolean existsByIdAndPatientId(Long userId,Long patientId);
}
