package com.example.demo.sharePrescription;

import com.example.demo.entity.userEntity.User;
import com.example.demo.sharePrescription.service.ShareEmergencyContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patient/sharedContact")
@RequiredArgsConstructor
public class ShareEmergencyContactController {
    private final ShareEmergencyContactService shareEmergencyContactService;

    @GetMapping
    public ResponseEntity<?> getEmergencyPatients(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(shareEmergencyContactService.getPatientsForShareEmergencyContact(user.getEmail()));
    }
}
