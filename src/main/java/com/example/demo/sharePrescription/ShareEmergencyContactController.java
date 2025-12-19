package com.example.demo.sharePrescription;

import com.example.demo.ShareToken.ShareTokenService;
import com.example.demo.addMedication.dto.SuperPrescriptionResponseDto;
import com.example.demo.addMedication.services.HealthDataService;
import com.example.demo.entity.patientEntity.Patient;
import com.example.demo.entity.userEntity.User;
import com.example.demo.patientContact.repository.ContactTelecomRepository;
import com.example.demo.patientContact.repository.PatientContactRepository;
import com.example.demo.sharePrescription.service.ShareEmergencyContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patient/sharedContact")
@RequiredArgsConstructor
public class ShareEmergencyContactController {
    private final ShareEmergencyContactService shareEmergencyContactService;
    private final HealthDataService healthDataService;
    private final PatientContactRepository patientContactRepository;
    private final ContactTelecomRepository contactTelecomRepository;
    private final ShareTokenService shareTokenService;

    @GetMapping
    public ResponseEntity<?> getEmergencyPatients(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(shareEmergencyContactService.getPatientsForShareEmergencyContact(user.getEmail()));
    }



    @GetMapping("/view/{token}")
    public ResponseEntity<?> viewSharedPrescription(
            @PathVariable String token,
            @AuthenticationPrincipal User user) {

        ShareTokenService.TokenEntry entry =
                shareTokenService.validate(token, user.getEmail());

        if (entry == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        // extra safety (still enforce relationship)
        boolean allowed =
                contactTelecomRepository.existsEmergencyContactForPatient(
                        user.getEmail(),
                        entry.patientId()
                );

        if (!allowed) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String newToken = shareTokenService.rotate(token);

        return ResponseEntity.ok(
                Map.of(
                        "token", newToken,
                        "prescriptions",
                        healthDataService.getSuperPrescriptionByPatient(entry.patientId())
                )
        );
    }
}
