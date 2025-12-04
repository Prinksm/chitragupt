package com.example.demo.sharePrescription;

import com.example.demo.addMedication.dto.SuperPrescriptionResponseDto;
import com.example.demo.addMedication.services.HealthDataService;
import com.example.demo.entity.userEntity.User;
import com.example.demo.sharePrescription.service.ShareEmergencyContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient/sharedContact")
@RequiredArgsConstructor
public class ShareEmergencyContactController {
    private final ShareEmergencyContactService shareEmergencyContactService;
    private final HealthDataService healthDataService;

    @GetMapping
    public ResponseEntity<?> getEmergencyPatients(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(shareEmergencyContactService.getPatientsForShareEmergencyContact(user.getEmail()));
    }



    @GetMapping("/view/{patientId}")
    public ResponseEntity<List<SuperPrescriptionResponseDto>> getPrescriptionsByPatient(@PathVariable Long patientId) {

        List<SuperPrescriptionResponseDto> response = healthDataService.getSuperPrescriptionByPatient(patientId);

        return ResponseEntity.ok(response);
    }
}
