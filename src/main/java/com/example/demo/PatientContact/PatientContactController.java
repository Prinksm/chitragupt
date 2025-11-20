package com.example.demo.PatientContact;

import com.example.demo.PatientContact.dto.PatientContactDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/patient/patient-contacts")
public class PatientContactController {
    @Autowired
    private PatientContactService patientContactService;

    @PostMapping("/save")
    public ResponseEntity<PatientContactDto> addPatientContact(@RequestBody PatientContactDto patientContactDTO) {
        PatientContactDto savedContact = patientContactService.addPatientContact(patientContactDTO);
        return ResponseEntity.ok(savedContact);
    }

    @PutMapping("/update/{id}")
    //path variable id is id of patient contact
    public ResponseEntity<PatientContactDto> updatePatientContact(@PathVariable Long id, @RequestBody PatientContactDto patientContactDTO) {
        PatientContactDto updatedContact = patientContactService.updatePatientContact(id, patientContactDTO);
        return ResponseEntity.ok(updatedContact);
    }

    @GetMapping("/{patientId}")
    public ResponseEntity<List<PatientContactDto>> getPatientContacts(@PathVariable Long patientId) {
        List<PatientContactDto> patientContacts = patientContactService.getPatientContacts(patientId);
        return ResponseEntity.ok(patientContacts);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePatientContact(@PathVariable Long id) {
        patientContactService.deletePatientContact(id);
        return ResponseEntity.noContent().build();
    }
}
