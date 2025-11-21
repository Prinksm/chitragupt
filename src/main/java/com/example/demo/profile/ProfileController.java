package com.example.demo.profile;

import com.example.demo.profile.dto.PatientDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patient/profile")
public class ProfileController {
    private final ProfileAddService profileAddService;
    @PutMapping("/save/{patientId}")
    public ResponseEntity<PatientDto> addPatientBasicInfo(@PathVariable Long patientId , @RequestBody PatientDto patientDto) {
        PatientDto createdPatient = profileAddService.addPatientBasicInfo(patientId , patientDto);
        return ResponseEntity.ok(createdPatient);
    }

}
