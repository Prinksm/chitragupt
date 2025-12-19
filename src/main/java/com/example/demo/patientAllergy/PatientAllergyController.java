// src/main/java/com/example/demo/patientAllergy/PatientAllergyController.java
package com.example.demo.patientAllergy;

import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.example.demo.entity.userEntity.User;
import com.example.demo.patientAllergy.dto.PatientAllergyRequestDto;
import com.example.demo.patientAllergy.dto.PatientAllergyResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    @PostMapping("/no")
    public ResponseEntity<Void> markNoAllergy(@AuthenticationPrincipal User user){
        allergyService.markNoAllergy(user.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getAllergyStatus(@AuthenticationPrincipal User user){
        boolean answered = allergyService.getAllergyAnsweredStatus(user.getId());
        List<PatientAllergyResponseDto> allergies = allergyService.getAllergy(user.getId());
        // you can return both answered flag and existing allergies if you want
        return ResponseEntity.ok(Map.of(
                "hasAnswered", answered,
                "hasAllergies", !allergies.isEmpty(),
                "allergies", allergies
        ));
    }

    @GetMapping
    public ResponseEntity<List<PatientAllergyResponseDto>> getAllergy(@AuthenticationPrincipal User user){
        List<PatientAllergyResponseDto> response = allergyService.getAllergy(user.getId());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/delete/{slug}")
    public ResponseEntity<Void> deleteAllergy(@AuthenticationPrincipal User user, @PathVariable String slug){

        Long allergyId = AllergySlugUtil.fromSlug(slug);
        allergyService.deleteAllergy(user.getId(), allergyId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/update/{slug}")
    public ResponseEntity<PatientAllergyResponseDto> updateAllergy(
            @AuthenticationPrincipal User user,
            @PathVariable String slug,
            @RequestBody PatientAllergyRequestDto dto
    ) {
        Long allergyId = AllergySlugUtil.fromSlug(slug);
        var response = allergyService.updateAllergy(user.getId(), allergyId, dto);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{slug}")
    public ResponseEntity<PatientAllergyResponseDto> getOne(
            @AuthenticationPrincipal User user,
            @PathVariable String slug
    ){
        List<PatientAllergyResponseDto> list = allergyService.getAllergy(user.getId());

        Long allergyId = AllergySlugUtil.fromSlug(slug);
        return ResponseEntity.ok(
                list.stream().filter(a -> a.getId().equals(allergyId)).findFirst()
                        .orElseThrow(() -> new ResourceNotFoundException("Allergy not found"))
        );
    }

}
