package com.example.demo.sharePrescription.dto;

import lombok.Data;

import java.util.List;

@Data
public class ShareRequestDto {
    private List<Long> prescriptionIds;
    private Long telecomId;
    private Long contactId;
}
