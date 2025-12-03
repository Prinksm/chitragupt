package com.example.demo.shareFhirData.controller;

import com.example.demo.shareFhirData.services.ExportFhirMedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/patient/prescriptions")
public class ExportFhirMedicationsController {

    @Autowired
    private ExportFhirMedicationService medicationService;

    /**
     * Build and download a FHIR Bundle for selected prescriptions
     */
    @PostMapping("/bundle-export")
    public ResponseEntity<Resource> downloadMedicationBundle(@RequestBody List<Long> prescriptionIds) {

        // 1️⃣ Build the FHIR Bundle JSON
        String bundleJson = medicationService.buildMedicationBundle(prescriptionIds);
        System.out.println(bundleJson);
        // 2️⃣ Convert to Spring Resource
        ByteArrayResource resource = new ByteArrayResource(bundleJson.getBytes(StandardCharsets.UTF_8));

        // 3️⃣ Return as downloadable file
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=medications_bundle.json")
                .contentType(MediaType.APPLICATION_JSON)
                .contentLength(bundleJson.getBytes(StandardCharsets.UTF_8).length)
                .body(resource);
    }
}
