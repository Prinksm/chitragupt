package com.example.demo.profile;

import com.example.demo.entity.userEntity.User;
import com.example.demo.profile.dto.PatientDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patient/profile")
public class ProfileController {
    private final ProfileService profileService;
    @PutMapping("/save/{patientId}")
    public ResponseEntity<PatientDto> addPatientBasicInfo(@PathVariable Long patientId , @RequestBody PatientDto patientDto) {
        PatientDto createdPatient = profileService.addPatientBasicInfo(patientId , patientDto);
        return ResponseEntity.ok(createdPatient);
    }

    @GetMapping
    public ResponseEntity<PatientDto> getPatientProfile( @AuthenticationPrincipal User user) {
        PatientDto patientDto = profileService.getPatientProfile(user.getId());
        return ResponseEntity.ok(patientDto);
    }

    @DeleteMapping("/telecoms/{telecomId}")
    public ResponseEntity<Void> deletePatientTelecom(@AuthenticationPrincipal User user, @PathVariable Long telecomId){
        profileService.deletePatientTelecom(user.getId(),telecomId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/address/{addressId}")
    public ResponseEntity<Void> deletePatientAddress(@AuthenticationPrincipal User user, @PathVariable Long addressId){
        profileService.deletePatientAddress(user.getId(), addressId);
        return ResponseEntity.noContent().build();
    }

}
