package com.example.demo.controller;

import com.example.demo.services.DataImports.DoseFormExcelImportService;
import com.example.demo.services.DataImports.MedicineExcelImportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/data-extractor")
public class DataExtractor {
    @Autowired
    DoseFormExcelImportService doseFormExcelImportService;
    @Autowired
    MedicineExcelImportService medicineExcelImportService;

        @PostMapping("/upload")
        public ResponseEntity<String> uploadDoseFormExcel(@RequestParam("file") MultipartFile file) {
            try {
                doseFormExcelImportService.importDoseFormExcel(file);
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

}
