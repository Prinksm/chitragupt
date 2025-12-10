package com.example.demo.addMedication.repo;

import com.example.demo.entity.medications.Prescription;
import com.example.demo.entity.medications.SuperPrescription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SuperPrescriptionRepo extends JpaRepository<SuperPrescription,Long> {
    List<SuperPrescription> findByPatientId(Long patientId);
    boolean existsByPatientId(Long patientId);
    @Query("""
        SELECT u.email 
        FROM SuperPrescription sp
        JOIN Patient p ON sp.patientId = p.id
        JOIN User u ON p.user.id = u.id
        WHERE sp.superPrescriptionId = :superPrescriptionId
    """)
    String findPatientEmailByPrescriptionId(@Param("superPrescriptionId") Long superPrescriptionId);
}
