package com.example.demo.dailyMedications.Dto;

import com.example.demo.addMedication.dto.DosageResponseDto;
import com.example.demo.addMedication.dto.MedicationResponseDto;
import com.example.demo.addMedication.dto.TimingResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicationNormalized extends MedicationResponseDto {
    private String doctorName;
    private Long prescriptionId;
    private Long prescriptionConditionId;
    private Date prescriptionDate;
    private String conditionName;
    private String conditionNotes;

}
