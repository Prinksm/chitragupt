package com.example.demo.addMedication.repo;

import com.example.demo.entity.medications.PatientMedicationLogs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientMedicationLogsRepo extends JpaRepository<PatientMedicationLogs, Long> {

    List<PatientMedicationLogs> findByPatientId(Long patientId);
    List<PatientMedicationLogs> findByPatientIdAndCreatedAtBetween(
            Long patientId,
            LocalDateTime startOfDay,
            LocalDateTime endOfDay
    );
    Optional<PatientMedicationLogs> findByPatientIdAndSuperPrescriptionIdAndPrescriptionIdAndStatementIdAndDoseTime(
            Long patientId,
            Long superPrescriptionId,
            Long prescriptionId,
            Long statementId,
            LocalDateTime doseTime
    );
}
