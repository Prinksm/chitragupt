package com.example.demo.downloadPrescription;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/patient/prescriptions")
public class DownloadPrecriptionController {
@Autowired
DownloadPrescriptionService downloadPrescriptionService;

    @GetMapping("/download/{id}/pdf")
    public ResponseEntity<byte[]> downloadSuperPrescriptionPdf(@PathVariable Long id) {

        byte[] pdfData = downloadPrescriptionService.generateSuperPrescriptionPdf(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=super_prescription_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfData);
    }

}
