package com.example.demo.patientAllergy;

import com.example.demo.entity.userEntity.User;
import com.example.demo.patientAllergy.dto.PatientAllergyRequestDto;
import com.example.demo.patientAllergy.dto.PatientAllergyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/patient/allergy")
public class PatientAllergyController {
    private final PatientAllergyService allergyService;

    @PostMapping("/save")
    public ResponseEntity<PatientAllergyResponseDto> addAllergy(@AuthenticationPrincipal User user, @RequestBody PatientAllergyRequestDto dto){
        PatientAllergyResponseDto response = allergyService.addAllergy(user.getId(), dto);
        return ResponseEntity.status(201).body(response);
    }

    @GetMapping
    public ResponseEntity<List<PatientAllergyResponseDto>>getAllergy(@AuthenticationPrincipal User user){
        List<PatientAllergyResponseDto> response = allergyService.getAllergy(user.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{allergyId}")
    public ResponseEntity<Void>deleteAllergy(@AuthenticationPrincipal User user, @PathVariable Long allergyId){
        allergyService.deleteAllergy(user.getId(), allergyId);
        return ResponseEntity.noContent().build();
    }


}
