package com.example.demo.shareFhirData.controller;

import com.example.demo.entity.userEntity.User;
import com.example.demo.shareFhirData.services.ExportFhirMedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/patient/fhir")
public class ExportFhirMedicationsController {

    @Autowired
    private ExportFhirMedicationService medicationService;
//Bundle download for the medication Statement
    @PostMapping("/bundle-medication")
    public ResponseEntity<Resource> downloadMedicationBundle(@RequestBody List<Long> prescriptionIds) {


        String bundleJson = medicationService.buildMedicationBundle(prescriptionIds);
        System.out.println(bundleJson);

        ByteArrayResource resource = new ByteArrayResource(bundleJson.getBytes(StandardCharsets.UTF_8));

        // 3️⃣ Return as downloadable file
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=medications_bundle.json")
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(bundleJson.getBytes(StandardCharsets.UTF_8).length)
                .body(resource);
    }
    @PostMapping("/bundle-patient")
    public ResponseEntity<Resource> downloadPatient(@AuthenticationPrincipal User user) {

        Long userId = user.getId();
        String bundleJson = medicationService.buildPatientBundle(userId);
        System.out.println(bundleJson);

        ByteArrayResource resource = new ByteArrayResource(bundleJson.getBytes(StandardCharsets.UTF_8));

        // 3️⃣ Return as downloadable file
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=medications_bundle.json")
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(bundleJson.getBytes(StandardCharsets.UTF_8).length)
                .body(resource);
    }
}
