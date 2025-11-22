package com.example.demo.addMedication.repo;

import com.example.demo.entity.medications.MedicationStatements;
import com.example.demo.entity.medications.Medications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicationStatementRepo extends JpaRepository<MedicationStatements,Long> {
    List<MedicationStatements> findByPrescriptionId(Long prescriptionId);
    List<MedicationStatements> findByFhirJsonIsNull();
}
