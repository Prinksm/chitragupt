package com.example.demo.sharePrescription.service;


import com.example.demo.ShareToken.ShareTokenService;
import com.example.demo.addMedication.services.HealthDataService;
import com.example.demo.entity.patientEntity.ContactTelecom;
import com.example.demo.entity.patientEntity.PatientContact;
import com.example.demo.patientContact.repository.ContactTelecomRepository;
import com.example.demo.sharePrescription.dto.ShareContactPatientDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShareEmergencyContactService {
    private final ContactTelecomRepository contactTelecomRepo;
    private final HealthDataService healthDataService;
    private final ShareTokenService shareTokenService;

    public List<ShareContactPatientDto> getPatientsForShareEmergencyContact(String email) {

        List<ContactTelecom> telecoms = contactTelecomRepo.findByEmail(email);

        return telecoms.stream()
                .map(ContactTelecom::getContact)
                .map(PatientContact::getPatient)
                .distinct()
                .map(patient -> {

                    ShareContactPatientDto dto = new ShareContactPatientDto();
                    dto.setShareToken(
                            shareTokenService.createToken(patient.getId(), email)
                    );
                    dto.setFirstName(patient.getFirstName());
                    dto.setMiddleName(patient.getMiddleName());
                    dto.setLastName(patient.getLastName());

//                    dto.setPrescriptions(
//                            healthDataService.getSuperPrescriptionByPatient(patient.getId())
//                    );

                    return dto;
                })
                .toList();
    }


}
