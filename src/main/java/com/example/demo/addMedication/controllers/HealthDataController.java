package com.example.demo.addMedication.controllers;

import com.example.demo.addMedication.dto.PrescriptionDto;
import com.example.demo.addMedication.dto.SuperPrescriptionDto;
import com.example.demo.addMedication.dto.SuperPrescriptionResponseDto;
import com.example.demo.addMedication.repo.SuperPrescriptionRepo;
import com.example.demo.addMedication.services.HealthDataService;
import com.example.demo.entity.userEntity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patient/prescriptions")
public class HealthDataController {


    @Autowired
    private HealthDataService healthDataService;

    @Autowired
    private SuperPrescriptionRepo superPrescriptionRepo;
    @PostMapping("/add-prescriptions")
    public ResponseEntity<Map<String, String>> createPrescriptions(@RequestBody SuperPrescriptionDto prescription,
                                                                                 @AuthenticationPrincipal User user) {

        Long userId = user.getId();

        prescription.setPatientId(userId);
        System.out.println(prescription);
        healthDataService.savePrescription(prescription);
        Map<String, String> response = Map.of("message", "All prescriptions saved successfully");
        return ResponseEntity.ok(response);

    }

    @GetMapping("/prescription")
    public ResponseEntity<List<SuperPrescriptionResponseDto>> getSuperPrescription(@AuthenticationPrincipal User user) {

        Long userId = user.getId();
        System.out.println("userId: " + userId);

        // Retrieve prescriptions
        List<SuperPrescriptionResponseDto> prescriptions = healthDataService.getSuperPrescriptionByPatient(userId);

        // Return the list as the response body
        return ResponseEntity.ok(prescriptions);
    }

    //Checks the prescription exists
    @GetMapping("/exists")
    public ResponseEntity<Map<String, Boolean>> prescriptionExists(@AuthenticationPrincipal User user) {
        Long userId = user.getId();
        boolean exists = superPrescriptionRepo.existsByPatientId(userId);
        return ResponseEntity.ok(Map.of("exists", exists));
    }

}


