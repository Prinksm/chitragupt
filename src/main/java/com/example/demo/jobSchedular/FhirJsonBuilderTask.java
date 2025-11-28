package com.example.demo.jobSchedular;

import com.example.demo.services.fhirBuilder.MedicationFhirBuilderService;
import com.example.demo.services.fhirBuilder.MedicationStatementBuilderService;
import com.example.demo.services.fhirBuilder.PatientFhirBuilderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicBoolean;


@Component
public class FhirJsonBuilderTask  {

    @Autowired
    private MedicationFhirBuilderService medicationFhirBuilderService;
    @Autowired
    private  PatientFhirBuilderService patientFhirBuilderService;

    @Autowired
    private MedicationStatementBuilderService medicationStatementBuilderService;
    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    @Scheduled(initialDelay = 5000)
    public void medicationExecute() {

            medicationFhirBuilderService.MedicationFhirAdd();

    }
    @Scheduled(cron = "* 0 12 * * * ")
    public void medicationStatementExecute() {

        medicationStatementBuilderService.addMedicationStatementFhir();

    }


    @Scheduled(cron = "* 0 10 * * * ")
    public void executePatientFhirBuilder() {
        if (!isRunning.get()) {
            isRunning.set(true);
            try {
                patientFhirBuilderService.buildPatientFhirJson();
            } finally {
                isRunning.set(false);
            }
        }
    }
//    @Scheduled(cron = "0 12 * * *") // Cron expression for running  at 12:00 every day
//    public void medicationStatement() {
//        medicationFhirBuilderService.MedicationFhirAdd();
//    }
}