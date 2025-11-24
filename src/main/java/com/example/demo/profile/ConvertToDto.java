package com.example.demo.profile;

import com.example.demo.profile.dto.PatientAddressDto;
import com.example.demo.profile.dto.PatientDto;
import com.example.demo.profile.dto.PatientTelecomDto;
import com.example.demo.entity.patientEntity.Patient;
import com.example.demo.entity.patientEntity.PatientAddress;
import com.example.demo.entity.patientEntity.PatientTelecom;

import java.util.ArrayList;
import java.util.List;

public class ConvertToDto {
    public PatientDto convertToDto(Patient patient){
        PatientDto dto = new PatientDto();
        dto.setId(patient.getId());
        dto.setFirstName(patient.getFirstName());
        dto.setMiddleName(patient.getMiddleName());
        dto.setLastName(patient.getLastName());
        dto.setBirthDate(patient.getBirthDate());
        dto.setGender(patient.getGender());
        dto.setMaritalStatus(patient.getMaritalStatus());

        List<PatientTelecomDto> telecomDtos = new ArrayList<>();
        for (PatientTelecom patientTelecom : patient.getPatientTelecoms()) {
            PatientTelecomDto telecomDto = new PatientTelecomDto();
            telecomDto.setId(patientTelecom.getId());
            telecomDto.setSystem(patientTelecom.getTelecom().getSystem());
            telecomDto.setValue(patientTelecom.getTelecom().getValue());
            telecomDtos.add(telecomDto);
        }
        dto.setTelecoms(telecomDtos);

        List<PatientAddressDto>addressDtos = new ArrayList<>();
        for(PatientAddress patientAddress : patient.getPatientAddresses()){
            PatientAddressDto addressDto = new PatientAddressDto();
            addressDto.setId(patientAddress.getId());
            addressDto.setUseCode(patientAddress.getAddress().getUseCode());
            addressDto.setAddressType(patientAddress.getAddress().getAddressType());
            addressDto.setText(patientAddress.getAddress().getText());
            addressDto.setLine1(patientAddress.getAddress().getLine1());
            addressDto.setLine2(patientAddress.getAddress().getLine2());
            addressDto.setCity(patientAddress.getAddress().getCity());
            addressDto.setDistrict(patientAddress.getAddress().getDistrict());
            addressDto.setState(patientAddress.getAddress().getState());
            addressDto.setPostalCode(patientAddress.getAddress().getPostalCode());
            addressDto.setCountry(patientAddress.getAddress().getCountry());
            addressDtos.add(addressDto);
        }
        dto.setAddresses(addressDtos);

        return dto;

    }
}
