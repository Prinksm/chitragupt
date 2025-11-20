package com.example.demo.PatientContact.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContactAddressDto {
    private Long id;
    private String useCode;
    private String addressType;
    private String text;
    private String line1;
    private String line2;
    private String city;
    private String district;
    private String state;
    private String postalCode;
    private String country;
}
