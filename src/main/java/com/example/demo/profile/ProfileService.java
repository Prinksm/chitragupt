package com.example.demo.profile;

import com.example.demo.profile.dto.PatientAddressDto;
import com.example.demo.profile.dto.PatientDto;
import com.example.demo.profile.dto.PatientTelecomDto;
import com.example.demo.profile.repository.PatientAddressRepository;
import com.example.demo.profile.repository.PatientTelecomRepository;
import com.example.demo.entity.patientEntity.Patient;
import com.example.demo.entity.patientEntity.PatientAddress;
import com.example.demo.entity.patientEntity.PatientTelecom;
import com.example.demo.entity.userEntity.CommonAddress;
import com.example.demo.entity.userEntity.CommonTelecom;
import com.example.demo.repository.AuthCommon.CommonAddressRepository;
import com.example.demo.repository.AuthCommon.CommonTelecomRepository;
import com.example.demo.profile.repository.PatientRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final PatientRepository patientRepository;
    private final PatientAddressRepository patientAddressRepository;
    private final PatientTelecomRepository patientTelecomRepository;
    private final CommonAddressRepository commonAddressRepository;
    private final CommonTelecomRepository commonTelecomRepository;

    public PatientDto addPatientBasicInfo(Long patientId, PatientDto patientDto){
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

        if (patientDto.getFirstName() != null) {
            patient.setFirstName(patientDto.getFirstName());
        }
        if (patientDto.getMiddleName() != null) {
            patient.setMiddleName(patientDto.getMiddleName());
        }
        if (patientDto.getLastName() != null) {
            patient.setLastName(patientDto.getLastName());
        }
        if (patientDto.getBirthDate() != null) {
            patient.setBirthDate(patientDto.getBirthDate());
        }
        if (patientDto.getGender() != null) {
            patient.setGender(patientDto.getGender());
        }
        if (patientDto.getMaritalStatus() != null) {
            patient.setMaritalStatus(patientDto.getMaritalStatus());
        }
        patient = patientRepository.save(patient);


        if (patientDto.getTelecoms() != null && !patientDto.getTelecoms().isEmpty()) {
            List<PatientTelecom> telecoms = new ArrayList<>();
            for (PatientTelecomDto telecomDto : patientDto.getTelecoms()) {
                if (telecomDto.getId() != null) {
                    PatientTelecom existingTelecom = patientTelecomRepository.findById(telecomDto.getId())
                            .orElseThrow(() -> new RuntimeException("PatientTelecom not found with id: " + telecomDto.getId()));

                    CommonTelecom commonTelecom = existingTelecom.getTelecom();
                    if (telecomDto.getSystem() != null) {
                        commonTelecom.setSystem(telecomDto.getSystem());
                    }
                    if (telecomDto.getValue() != null) {
                        commonTelecom.setValue(telecomDto.getValue());
                    }
                    if (telecomDto.getUse() != null) {
                        commonTelecom.setUseCode(telecomDto.getUse());
                    }
                    commonTelecom = commonTelecomRepository.save(commonTelecom);
                } else {
                    CommonTelecom commonTelecom = new CommonTelecom();
                    commonTelecom.setSystem(telecomDto.getSystem());
                    commonTelecom.setValue(telecomDto.getValue());
                    commonTelecom.setUseCode(telecomDto.getUse());
                    commonTelecom = commonTelecomRepository.save(commonTelecom);

                    PatientTelecom patientTelecom = new PatientTelecom();
                    patientTelecom.setPatient(patient);
                    patientTelecom.setTelecom(commonTelecom);
                    patientTelecom = patientTelecomRepository.save(patientTelecom);

                    if (patient.getPatientTelecoms() == null) {
                        patient.setPatientTelecoms(new HashSet<>());
                    }
                    patient.getPatientTelecoms().add(patientTelecom);
                }
            }
        }

        if (patientDto.getAddresses() != null && !patientDto.getAddresses().isEmpty()) {
            List<PatientAddress> addresses = new ArrayList<>();
            for (PatientAddressDto addressDto : patientDto.getAddresses()) {
                if (addressDto.getId() != null) {
                    // Update existing address
                    PatientAddress existingAddress = patientAddressRepository.findById(addressDto.getId())
                            .orElseThrow(() -> new RuntimeException("PatientAddress not found with id: " + addressDto.getId()));

                    CommonAddress commonAddress = existingAddress.getAddress();
                    if (addressDto.getUseCode() != null) {
                        commonAddress.setUseCode(addressDto.getUseCode());
                    }
                    if (addressDto.getAddressType() != null) {
                        commonAddress.setAddressType(addressDto.getAddressType());
                    }
                    if (addressDto.getText() != null) {
                        commonAddress.setText(addressDto.getText());
                    }
                    if (addressDto.getLine1() != null) {
                        commonAddress.setLine1(addressDto.getLine1());
                    }
                    if (addressDto.getLine2() != null) {
                        commonAddress.setLine2(addressDto.getLine2());
                    }
                    if (addressDto.getCity() != null) {
                        commonAddress.setCity(addressDto.getCity());
                    }
                    if (addressDto.getDistrict() != null) {
                        commonAddress.setDistrict(addressDto.getDistrict());
                    }
                    if (addressDto.getState() != null) {
                        commonAddress.setState(addressDto.getState());
                    }
                    if (addressDto.getPostalCode() != null) {
                        commonAddress.setPostalCode(addressDto.getPostalCode());
                    }
                    if (addressDto.getCountry() != null) {
                        commonAddress.setCountry(addressDto.getCountry());
                    }
                    commonAddress = commonAddressRepository.save(commonAddress);
                } else {
                    // Add new address
                    CommonAddress commonAddress = new CommonAddress();
                    commonAddress.setUseCode(addressDto.getUseCode());
                    commonAddress.setAddressType(addressDto.getAddressType());
                    commonAddress.setText(addressDto.getText());
                    commonAddress.setLine1(addressDto.getLine1());
                    commonAddress.setLine2(addressDto.getLine2());
                    commonAddress.setCity(addressDto.getCity());
                    commonAddress.setDistrict(addressDto.getDistrict());
                    commonAddress.setState(addressDto.getState());
                    commonAddress.setPostalCode(addressDto.getPostalCode());
                    commonAddress.setCountry(addressDto.getCountry());
                    commonAddress = commonAddressRepository.save(commonAddress);

                    PatientAddress patientAddress = new PatientAddress();
                    patientAddress.setPatient(patient);
                    patientAddress.setAddress(commonAddress);
                    patientAddress = patientAddressRepository.save(patientAddress);

                    if (patient.getPatientAddresses() == null) {
                        patient.setPatientAddresses(new HashSet<>());
                    }
                    patient.getPatientAddresses().add(patientAddress);
                }
            }
        }

        patient = patientRepository.save(patient);

        ConvertToDto todto = new ConvertToDto();
       return todto.convertToDto(patient);
    }



    @Transactional
    public PatientDto getPatientProfile(Long patientId) {
        try {
            Patient patient = patientRepository.findById(patientId)
                    .orElseThrow(() -> new RuntimeException("Patient not found with id: " + patientId));

            boolean hasBasicInfo =
                    patient.getBirthDate() != null &&
                            patient.getGender() != null &&
                            patient.getMaritalStatus() != null;

            boolean hasAddress = patient.getPatientAddresses() != null && !patient.getPatientAddresses().isEmpty();
            boolean hasTelecom = patient.getPatientTelecoms() != null && !patient.getPatientTelecoms().isEmpty();

            if (!hasBasicInfo || !hasAddress || !hasTelecom) {
                return null;
            }

            ConvertToDto toDto = new ConvertToDto();
            return toDto.convertToDto(patient);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get patient profile: " + e.getMessage(), e);
        }
    }

    @Transactional
    public void deletePatientTelecom(Long patientId, Long telecomId) {

        PatientTelecom telecomToDelete = patientTelecomRepository.findByIdAndPatientId(telecomId, patientId)
                .orElseThrow(() -> new RuntimeException("Telecom not found with id: " + telecomId +
                        " for patient: " + patientId));
        long telecomCount = patientTelecomRepository.countByPatientId(patientId);
        if (telecomCount <= 1) {
            throw new IllegalStateException("Patient must have at least one telecom");
        }
        Long commonTelecomId = telecomToDelete.getTelecom().getId();
        patientTelecomRepository.delete(telecomToDelete);
        boolean isCommonTelecomStillUsed = patientTelecomRepository.existsByTelecomId(commonTelecomId);
        if (!isCommonTelecomStillUsed) {
            commonTelecomRepository.deleteById(commonTelecomId);
        }
    }

    @Transactional
    public void deletePatientAddress(Long patientId, Long addressId) {
        PatientAddress addressToDelete = patientAddressRepository.findByIdAndPatientId(addressId, patientId)
                .orElseThrow(() -> new RuntimeException("Address not found with id: " + addressId +
                        " for patient: " + patientId));

        long addressCount = patientAddressRepository.countByPatientId(patientId);
        if (addressCount <= 1) {
            throw new IllegalStateException("Patient must have at least one address");
        }

        Long commonAddressId = addressToDelete.getAddress().getId();
        patientAddressRepository.delete(addressToDelete);
        boolean isCommonAddressStillUsed = patientAddressRepository.existsByAddressId(commonAddressId);
        if (!isCommonAddressStillUsed) {
            commonAddressRepository.deleteById(commonAddressId);
        }
    }

}
