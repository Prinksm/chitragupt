package com.example.demo.addMedication.controllers;

import com.example.demo.addMedication.dto.PrescriptionDto;
import com.example.demo.addMedication.services.HealthDataService;
import com.example.demo.entity.userEntity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient/prescriptions")
public class HealthDataController {


    @Autowired
    private HealthDataService healthDataServicee;

    @PostMapping("/add-prescription")
    public ResponseEntity<?> createPrescription(@RequestBody PrescriptionDto request,
                                                @AuthenticationPrincipal User user) {

        Long userId = user.getId();
        request.setPatientId(userId);
        System.out.println("userId" + userId);

        healthDataServicee.savePrescription(request);
        return ResponseEntity.ok("Prescription saved successfully");
    }
    @GetMapping("/prescription")
    public ResponseEntity<List<PrescriptionDto>> getPrescription(@AuthenticationPrincipal User user) {

        Long userId = user.getId();
        System.out.println("userId: " + userId);

        // Retrieve prescriptions
        List<PrescriptionDto> prescriptions = healthDataServicee.getPrescriptionsByPatient(userId);

        // Return the list as the response body
        return ResponseEntity.ok(prescriptions);
    }

}


