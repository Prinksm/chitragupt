package com.example.demo.dailyMedications.Controller;

import com.example.demo.addMedication.dto.SuperPrescriptionResponseDto;
import com.example.demo.dailyMedications.Dto.MedicationWithStatus;
import com.example.demo.dailyMedications.Services.DailyMedicationService;
import com.example.demo.entity.userEntity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/patient/prescriptions")
public class DailyMedicationController {
    @Autowired
    DailyMedicationService dailyMedicationService;
    @GetMapping("/daily-meds")
    public ResponseEntity<List<MedicationWithStatus>> getSuperPrescription(@AuthenticationPrincipal User user) {

        Long userId = user.getId();
        System.out.println("userId: " + userId);
        List<MedicationWithStatus> dailyMeds = dailyMedicationService.getDailyMedications(userId);
        //Get the prescription
        return ResponseEntity.ok(dailyMeds);

    }
}
