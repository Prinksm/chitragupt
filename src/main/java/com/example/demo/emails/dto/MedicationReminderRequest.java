package com.example.demo.emails.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MedicationReminderRequest {
    private Long superPrescriptionId;
    private String medicationName;
    private String doseTime;
    private int reminderType;
}
