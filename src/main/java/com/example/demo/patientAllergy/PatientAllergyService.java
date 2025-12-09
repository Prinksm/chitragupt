package com.example.demo.patientAllergy;

import ca.uhn.fhir.rest.server.exceptions.ResourceNotFoundException;
import com.example.demo.entity.codeableConcept.Concepts;
import com.example.demo.entity.patientEntity.Patient;
import com.example.demo.entity.patientEntity.PatientAllergy;
import com.example.demo.patientAllergy.dto.PatientAllergyRequestDto;
import com.example.demo.patientAllergy.dto.PatientAllergyResponseDto;
import com.example.demo.patientAllergy.repository.PatientAllergyRepository;
import com.example.demo.profile.repository.PatientRepository;
import com.example.demo.repository.codeableConcept.ConceptRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PatientAllergyService {
    private final PatientRepository patientRepo;
    private final PatientAllergyRepository allergyRepo;
    private final AllergyCodeService allergyCodeService;
    private final ConceptRepo conceptRepo;

    public PatientAllergyResponseDto addAllergy(Long patientId, PatientAllergyRequestDto dto){
        Patient patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));


        patient.setAllergyAnswered(true);
        patientRepo.save(patient);


        long conceptId = allergyCodeService.getConditionID(dto.getAllergyName());
        PatientAllergy allergy = new PatientAllergy();
        allergy.setPatient(patient);
        allergy.setClinicalStatus(dto.getClinicalStatus());
        allergy.setVerificationStatus(dto.getVerificationStatus());
        allergy.setAllergyType(dto.getAllergyType());
        allergy.setCategory(dto.getCategory());
        allergy.setCriticality(dto.getCriticality());
        allergy.setOnsetDate(dto.getOnsetDate());
        allergy.setAllergyCode(conceptId);

        PatientAllergy saved = allergyRepo.save(allergy);
        return toResponseDto(saved, dto.getAllergyName());

    }


    @Transactional
    public void markNoAllergy(Long patientId){
        Patient patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));

        patient.setAllergyAnswered(true);
        patientRepo.save(patient);
    }


    @Transactional
    public void deleteAllergy(Long patientId, Long allergyId){
        PatientAllergy allergy = allergyRepo.findById(allergyId)
         .orElseThrow(() -> new ResourceNotFoundException("Allergy not found with id: " + allergyId));
        if (!allergy.getPatient().getId().equals(patientId)) {
            throw new IllegalArgumentException("Allergy does not belong to patient: " + patientId);
        }
        allergyRepo.delete(allergy);
    }


    @Transactional
    public PatientAllergyResponseDto updateAllergy(Long patientId, Long allergyId, PatientAllergyRequestDto dto) {

        PatientAllergy allergy = allergyRepo.findById(allergyId)
                .orElseThrow(() -> new ResourceNotFoundException("Allergy not found with id: " + allergyId));

        if (!allergy.getPatient().getId().equals(patientId)) {
            throw new IllegalArgumentException("Allergy does not belong to patient: " + patientId);
        }

        // Update allergy code if name changed
        if (dto.getAllergyName() != null) {
            long conceptId = allergyCodeService.getConditionID(dto.getAllergyName());
            allergy.setAllergyCode(conceptId);
        }

        allergy.setClinicalStatus(dto.getClinicalStatus());
        allergy.setVerificationStatus(dto.getVerificationStatus());
        allergy.setAllergyType(dto.getAllergyType());
        allergy.setCategory(dto.getCategory());
        allergy.setCriticality(dto.getCriticality());
        allergy.setOnsetDate(dto.getOnsetDate());

        PatientAllergy saved = allergyRepo.save(allergy);

        return toResponseDto(saved, dto.getAllergyName());
    }



    public List<PatientAllergyResponseDto> getAllergy(Long patientId){
        Patient patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        List<PatientAllergy> list = allergyRepo.findByPatientId(patientId);
        return list.stream()
                .map(a -> toResponseDto(a, null))
                .collect(Collectors.toList());
    }

    public boolean getAllergyAnsweredStatus(Long patientId) {
        Patient patient = patientRepo.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found with id: " + patientId));
        return Boolean.TRUE.equals(patient.getAllergyAnswered());
    }

    private PatientAllergyResponseDto toResponseDto(PatientAllergy entity, String allergyNameFromRequest) {

        String allergyName;
        if (allergyNameFromRequest != null) {
            allergyName = allergyNameFromRequest;
        } else {
            allergyName = conceptRepo.findById(entity.getAllergyCode())
                    .map(Concepts::getConceptName)
                    .orElse(null);
        }
        PatientAllergyResponseDto r = new PatientAllergyResponseDto();

        r.setId(entity.getId());
        r.setPatientId(entity.getPatient().getId());
        r.setConceptId(entity.getAllergyCode());
        r.setAllergyName(allergyName);
        r.setClinicalStatus(entity.getClinicalStatus());
        r.setVerificationStatus(entity.getVerificationStatus());
        r.setAllergyType(entity.getAllergyType());
        r.setCategory(entity.getCategory());
        r.setCriticality(entity.getCriticality());
        r.setOnsetDate(entity.getOnsetDate());
        r.setRecordedDate(entity.getRecordedDate());
        return r;
    }
}
