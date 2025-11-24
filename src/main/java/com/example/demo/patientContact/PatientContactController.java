package com.example.demo.patientContact;

import com.example.demo.entity.userEntity.User;
import com.example.demo.patientContact.dto.PatientContactDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patient/patient-contacts")
public class PatientContactController {
    @Autowired
    private PatientContactService patientContactService;

    @PostMapping("/save")
    public ResponseEntity<PatientContactDto> addPatientContact(@RequestBody PatientContactDto patientContactDTO) {
        PatientContactDto savedContact = patientContactService.addPatientContact(patientContactDTO);
        return ResponseEntity.status(201).body(savedContact);
    }

    @PutMapping("/update/{id}")
    //path variable id is id of patient contact
    public ResponseEntity<PatientContactDto> updatePatientContact(@PathVariable Long id, @RequestBody PatientContactDto patientContactDTO) {
        PatientContactDto updatedContact = patientContactService.updatePatientContact(id, patientContactDTO);
        return ResponseEntity.ok(updatedContact);
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getPatientContacts(@AuthenticationPrincipal User user) {
        List contacts = patientContactService.getPatientContacts(user.getId());
        if (contacts == null || contacts.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "hasContact", false,
                    "contacts", List.of()
            ));
        }

        return ResponseEntity.ok(Map.of(
                "hasContact", true,
                "contacts", contacts
        ));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deletePatientContact(@PathVariable Long id) {
        patientContactService.deletePatientContact(id);
        return ResponseEntity.noContent().build();
    }
}
