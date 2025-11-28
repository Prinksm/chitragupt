package com.example.demo.addMedication.controllers;

import com.example.demo.addMedication.dto.MedicationLogsDto;
import com.example.demo.addMedication.services.PatientMedicationLogService;
import com.example.demo.entity.medications.PatientMedicationLogs;
import com.example.demo.entity.userEntity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/patient/medication-logs")
public class MedicationLogs {

    @Autowired
    private PatientMedicationLogService logService;

    //    @PostMapping("/mark")
//    public ResponseEntity<PatientMedicationLogs> markMedication(
//            @RequestParam Long superPrescriptionId,
//            @RequestParam Long prescriptionId,
//            @RequestParam Long statementId,
//            @RequestParam Boolean taken,
//            @AuthenticationPrincipal User user) {
//
//        Long patientId = user.getId();
//        PatientMedicationLogs log = logService.markMedication(patientId, superPrescriptionId, prescriptionId, statementId, taken);
//        return ResponseEntity.ok(log);
//    }
    @PostMapping("/mark")
    public ResponseEntity<PatientMedicationLogs> markMedication(
            @RequestBody MedicationLogsDto request,
            @AuthenticationPrincipal User user) {

        Long patientId = user.getId();
        request.setPatientId(patientId);
        PatientMedicationLogs log = logService.markMedication(request);

        return ResponseEntity.ok(log);
    }


    @GetMapping("/all")
    public ResponseEntity<List<PatientMedicationLogs>> getPatientLogs(@AuthenticationPrincipal User user) {
        Long patientId = user.getId();
        List<PatientMedicationLogs> logs = logService.getLogsByPatient(patientId);
        return ResponseEntity.ok(logs);
    }

    @GetMapping("/today")
    public ResponseEntity<List<PatientMedicationLogs>> getTodayLogs(
            @AuthenticationPrincipal User user) {

        Long patientId = user.getId();
        List<PatientMedicationLogs> logs = logService.getTodaysLogs(patientId);

        return ResponseEntity.ok(logs);
    }


}
