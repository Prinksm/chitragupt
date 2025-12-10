package com.example.demo.addMedication.services;

import com.example.demo.addMedication.dto.MedicationLogsDto;
import com.example.demo.addMedication.repo.PatientMedicationLogsRepo;
import com.example.demo.entity.medications.PatientMedicationLogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PatientMedicationLogService {

    @Autowired
    private PatientMedicationLogsRepo logRepository;

    public PatientMedicationLogs markMedication(MedicationLogsDto request) {

        // Check if log already exists
        Optional<PatientMedicationLogs> optionalLog = logRepository
                .findByPatientIdAndSuperPrescriptionIdAndPrescriptionIdAndStatementIdAndDoseTime(
                        request.getPatientId(),
                        request.getSuperPrescriptionId(),
                        request.getPrescriptionId(),
                        request.getStatementId(),
                        request.getDoseTime()
                );


        PatientMedicationLogs log;
        if (optionalLog.isPresent()) {
            log = optionalLog.get();
            log.setTaken(request.getTaken());
            log.setUpdatedAt(LocalDateTime.now());
        } else {
            log = new PatientMedicationLogs();
            log.setPatientId(request.getPatientId());
            log.setSuperPrescriptionId(request.getSuperPrescriptionId());
            log.setPrescriptionId(request.getPrescriptionId());
            log.setStatementId(request.getStatementId());
            log.setTaken(request.getTaken());
            log.setDoseTime(request.getDoseTime());
            System.out.println(request.getDoseTime());
            log.setCreatedAt(LocalDateTime.now());
            log.setUpdatedAt(LocalDateTime.now());
        }

        return logRepository.save(log);
    }

    public List<PatientMedicationLogs> getLogsByPatient(Long patientId) {
        return logRepository.findByPatientId(patientId);
    }
    public List<PatientMedicationLogs> getTodaysLogs(Long patientId) {

        LocalDate today = LocalDate.now();

        // Start of today: 00:00:00
        LocalDateTime startOfDay = today.atStartOfDay();

        // End of today: 23:59:59.999 → or simpler: tomorrow 00:00
        LocalDateTime endOfDay = today.plusDays(1).atStartOfDay();

        return logRepository.findByPatientIdAndCreatedAtBetween(
                patientId,
                startOfDay,
                endOfDay
        );
    }

}
