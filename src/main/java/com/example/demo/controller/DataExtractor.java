package com.example.demo.controller;

import com.example.demo.addMedication.dto.ConditionDto;
import com.example.demo.addMedication.services.MedicalConditionService;
import com.example.demo.services.DataImports.AllergyCodesExcelImportService;
import com.example.demo.services.DataImports.DoseFormExcelImportService;
import com.example.demo.services.DataImports.MedStatementImportService;
import com.example.demo.services.DataImports.MedicineExcelImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/data-extractor")
public class DataExtractor {
    @Autowired
    DoseFormExcelImportService doseFormExcelImportService;
    @Autowired
    MedicineExcelImportService medicineExcelImportService;
    @Autowired
    AllergyCodesExcelImportService allergyCodesExcelImportService;
    @Autowired
    MedStatementImportService medStatementImportService;

    @Autowired
    MedicalConditionService prescriptionService;

//    @GetMapping("/search")
//    public ConditionDto searchCondition(@RequestParam String conditionName) {
//        // Call the service to fetch ICD-10 code and names
//        Concept result = prescriptionService.fetchConditionAndSave(conditionName);
//
//        System.out.println(result.getConceptName()+"Concept Name");
//        System.out.println(result.getDescription()+"Description");
//        System.out.println(result.getIcd10Code()+"Code");
//        // Return the result map as JSON
//        return result;
//    }
    @PostMapping("/upload-doseForm")
    public ResponseEntity<String> uploadDoseFormExcel(@RequestParam("file") MultipartFile file) {
        try {
            doseFormExcelImportService.importDoseFormExcel(file);
            return ResponseEntity.ok("DoseForm Excel imported successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(":x: Error: " + e.getMessage());
        }
    }
    @PostMapping("/upload-routes")
    public ResponseEntity<String> uploadRouteExcel(@RequestParam("file") MultipartFile file) {
        try {
           medStatementImportService.importRouteCodeExcel(file);
            return ResponseEntity.ok("DoseForm Excel imported successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(":x: Error: " + e.getMessage());
        }
    }
    @PostMapping("/upload-amount-code")
    public ResponseEntity<String> uploadAmountCodeExcel(@RequestParam("file") MultipartFile file) {
        try {
            medStatementImportService.importAmountCodeExcel(file);
            return ResponseEntity.ok("DoseForm Excel imported successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(":x: Error: " + e.getMessage());
        }
    }
    @PostMapping("/upload-units")
    public ResponseEntity<String> uploadUnitExcel(@RequestParam("file") MultipartFile file) {
        try {
            doseFormExcelImportService.importUnitsExcel(file);
            return ResponseEntity.ok("DoseForm Excel imported successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(":x: Error: " + e.getMessage());
        }
    }

    @PostMapping("/upload-when-code")
    public ResponseEntity<String> uploadWhenCodeExcel(@RequestParam("file") MultipartFile file) {
        try {
            medStatementImportService.importWhenCodeExcel(file);
            return ResponseEntity.ok("DoseForm Excel imported successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(":x: Error: " + e.getMessage());
        }
    }

    @PostMapping("/upload-medicines")
    public ResponseEntity<String> uploadMedicineExcel(@RequestParam("file") MultipartFile file) {
        try {
            medicineExcelImportService.importMedicineExcel(file);
            return ResponseEntity.ok("DoseForm Excel imported successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(":x: Error: " + e.getMessage());
        }
    }

    @PostMapping("/upload-allergyCodes")
    public ResponseEntity<String> uploadAllergyCodesExcel(@RequestParam("file") MultipartFile file) {
        try {
            allergyCodesExcelImportService.importAllergyCodesExcel(file);
            return ResponseEntity.ok("Allergy Codes Excel imported successfully!");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body(":x: Error: " + e.getMessage());
        }
    }

}
