package com.example.demo.jobSchedular;

import com.example.demo.services.fhirBuilder.MedicationFhirBuilderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;


@Component
public class FhirJsonBuilderTask  {

    @Autowired
    private MedicationFhirBuilderService medicationFhirBuilderService;

    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    @Scheduled(initialDelay = 5000)
    public void medicationExecute() {

            medicationFhirBuilderService.MedicationFhirAdd();

    }
//    @Scheduled(cron = "0 12 * * *") // Cron expression for running  at 12:00 every day
//    public void medicationStatement() {
//        medicationFhirBuilderService.MedicationFhirAdd();
//    }
}