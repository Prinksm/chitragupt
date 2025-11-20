package com.example.demo.PatientContact.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientContactDto {
    private Long id;
    private Long patientId;
    private String relationshipType;
    private String firstName;
    private String MiddleName;
    private String lastName;
    private List<ContactAddressDto> contactAddresses;
    private List<ContactTelecomDto> contactTelecoms;
}
