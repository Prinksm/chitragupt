package com.example.demo.emails.services;




import com.example.demo.addMedication.repo.SuperPrescriptionRepo;
import com.example.demo.emails.template.EmailTemplates;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MedicationReminderService {

    private final SuperPrescriptionRepo superPrescriptionRepository;
    private final EmailService emailService;
    private final EmailTemplates emailTemplates;

    public void sendReminder(Long superPrescriptionId, String medicationName, String doseTime, int reminderType) {

        // fetch patient email using prescription id
        String email = superPrescriptionRepository.findPatientEmailByPrescriptionId(superPrescriptionId);

        if (email == null) {
            throw new RuntimeException("Patient email not found for prescription ID: " + superPrescriptionId);
        }

        // send reminder email
        emailTemplates.sendMedicationReminder(email, medicationName, doseTime, reminderType);
    }
}

