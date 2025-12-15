package com.example.demo.dailyMedications.Services;

import com.example.demo.addMedication.dto.MedicationResponseDto;
import com.example.demo.addMedication.dto.PrescriptionResponseDto;
import com.example.demo.addMedication.dto.SuperPrescriptionResponseDto;
import com.example.demo.addMedication.services.HealthDataService;
import com.example.demo.addMedication.services.PatientMedicationLogService;
import com.example.demo.dailyMedications.Dto.MedicationNormalized;
import com.example.demo.dailyMedications.Dto.MedicationWithStatus;
import com.example.demo.entity.medications.PatientMedicationLogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class DailyMedicationService {

    @Autowired
    HealthDataService healthDataService;
    @Autowired
    PatientMedicationLogService logService;

    public List<MedicationWithStatus> getDailyMedications(Long patientId) {
        List<MedicationNormalized> normalized = getMedicationNormalized(patientId);
        List<MedicationWithStatus> medsWithStatus = initializeMedicationWithStatus(normalized);
        List<PatientMedicationLogs> logs = logService.getTodaysLogs(patientId);

        // Generate today's doses and apply logs
        List<MedicationWithStatus> todayDoses = applyLogsToMeds(medsWithStatus, logs);
        return todayDoses; // ✅ return the doses, not just meds metadata
    }

    private List<MedicationWithStatus> applyLogsToMeds(
            List<MedicationWithStatus> meds,
            List<PatientMedicationLogs> logs) {

        List<MedicationWithStatus> allDoses = new ArrayList<>();

        for (MedicationWithStatus med : meds) {
            // Generate today's doses
            List<MedicationWithStatus> doses = generateDosesForToday(med);

            // Match logs
            for (MedicationWithStatus dose : doses) {
                for (PatientMedicationLogs log : logs) {
                    boolean match = log.getSuperPrescriptionId().equals(dose.getPrescriptionId()) &&
                            log.getPrescriptionId().equals(dose.getPrescriptionConditionId()) &&
                            log.getStatementId().equals(dose.getStatementId()) &&
                            log.getDoseTime() != null &&
                            log.getDoseTime().equals(dose.getDoseTime()); // ✅ use equals

                    if (match) {
                        dose.setTaken(log.getTaken());
                        dose.setTakenStatus(log.getTaken() ? MedicationWithStatus.DoseStatus.TAKEN
                                : MedicationWithStatus.DoseStatus.SKIPPED);
                        dose.setLogCreatedAt(log.getDoseTime());
                    }
                }
            }

            allDoses.addAll(doses);
        }

        // Sort by dose time
        allDoses.sort(Comparator.comparing(MedicationWithStatus::getDoseTime));

        return allDoses;
    }

    public List<MedicationWithStatus> generateDosesForToday(MedicationWithStatus med) {
        List<MedicationWithStatus> doses = new ArrayList<>();

        if (med.getTiming() == null || med.getTiming().getTimeOfDay() == null
                || med.getTiming().getFrequency() == null) {
            return doses;
        }

        int frequency = med.getTiming().getFrequency();
        double period = med.getTiming().getPeriod().doubleValue();
        String unit = med.getTiming().getPeriodUnit().toLowerCase();

        LocalDate today = LocalDate.now();
        LocalDateTime todayStart = today.atStartOfDay();
        LocalDateTime todayEnd = today.atTime(23, 59, 59, 999_000_000);

        // Convert effective start and end dates
        LocalDateTime effectiveStart = med.getEffectiveStartDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        // Make effectiveEnd inclusive for the whole day
        LocalDate effectiveEndDate = med.getEffectiveEndDate().toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        LocalDateTime effectiveEnd = effectiveEndDate.atTime(23, 59, 59, 999_000_000);

        // Convert period to Duration
        Duration periodDuration = switch (unit) {

            case "second" -> Duration.ofSeconds((long) period);
            case "minute" -> Duration.ofSeconds((long) (period * 60));
            case "hour" -> Duration.ofSeconds((long) (period * 3600));
            case "day" -> Duration.ofSeconds((long) (period * 86400));
            case "week" -> Duration.ofSeconds((long) (period * 86400 * 7));
            case "month" -> Duration.ofSeconds((long) (period * 86400 * 30));
            case "year" -> Duration.ofSeconds((long) (period * 86400 * 365));
            default -> Duration.ofDays((long) period);
        };

        // Start time for the first period
        LocalTime startTime = med.getTiming().getTimeOfDay().toLocalTime();
        LocalDateTime periodStart = today.atTime(startTime);

        // Adjust periodStart to effectiveStart if it is earlier
        if (periodStart.isBefore(effectiveStart))
            periodStart = effectiveStart;

        // Generate doses
        while (!periodStart.isAfter(todayEnd) && !periodStart.isAfter(effectiveEnd)) {
            Duration interval = (frequency > 1) ? periodDuration.dividedBy(frequency) : Duration.ZERO;

            for (int i = 0; i < frequency; i++) {
                LocalDateTime doseTime = periodStart.plus(interval.multipliedBy(i));
                if (doseTime.isAfter(todayEnd) || doseTime.isAfter(effectiveEnd))
                    break;

                MedicationWithStatus dose = new MedicationWithStatus();
                dose.copyFrom(med);
                dose.setDoseTime(doseTime);
                dose.setTaken(false);
                dose.setTakenStatus(MedicationWithStatus.DoseStatus.PENDING);
                doses.add(dose);
            }

            periodStart = periodStart.plus(periodDuration);
        }

        doses.sort(Comparator.comparing(MedicationWithStatus::getDoseTime));
        return doses;
    }

    public List<MedicationNormalized> getMedicationNormalized(Long patientId) {

        List<SuperPrescriptionResponseDto> prescriptions = healthDataService.getSuperPrescriptionByPatient(patientId);

        List<MedicationNormalized> meds = new ArrayList<>();

        for (SuperPrescriptionResponseDto entry : prescriptions) {

            for (PrescriptionResponseDto condition : entry.getPrescriptions()) {

                for (MedicationResponseDto med : condition.getMedications()) {

                    MedicationNormalized normalized = new MedicationNormalized();

                    normalized.setDoctorName(entry.getDoctorName());
                    normalized.setPrescriptionId(entry.getSuperPrescriptionId());
                    normalized.setPrescriptionDate(entry.getPrescriptionDate());
                    normalized.setConditionName(condition.getConditionName());
                    normalized.setConditionNotes(condition.getNotes());
                    normalized.setPrescriptionConditionId(condition.getPrescriptionId());
                    normalized.setStatementId(med.getStatementId());
                    normalized.setMedication(med.getMedication());
                    normalized.setMedicationId(med.getMedicationId());
                    normalized.setStatus(med.getStatus());
                    normalized.setEffectiveStartDate(med.getEffectiveStartDate());
                    normalized.setEffectiveEndDate(med.getEffectiveEndDate());
                    normalized.setNotes(med.getNotes());
                    normalized.setDosage(med.getDosage());
                    normalized.setTiming(med.getTiming());

                    meds.add(normalized);
                }
            }
        }

        return meds;
    }

    public List<MedicationWithStatus> initializeMedicationWithStatus(List<MedicationNormalized> meds) {

        List<MedicationWithStatus> result = new ArrayList<>();

        for (MedicationNormalized m : meds) {
            MedicationWithStatus w = new MedicationWithStatus();

            // copy parent fields (MedicationNormalized extends MedicationResponseDto)
            w.setDoctorName(m.getDoctorName());
            w.setPrescriptionId(m.getPrescriptionId());
            w.setPrescriptionConditionId(m.getPrescriptionConditionId());
            w.setPrescriptionDate(m.getPrescriptionDate());
            w.setConditionName(m.getConditionName());
            w.setConditionNotes(m.getConditionNotes());

            // fields from MedicationResponseDto
            w.setStatementId(m.getStatementId());
            w.setMedication(m.getMedication());
            w.setMedicationId(m.getMedicationId());
            w.setEffectiveStartDate(m.getEffectiveStartDate());
            w.setEffectiveEndDate(m.getEffectiveEndDate());
            w.setNotes(m.getNotes());
            w.setDosage(m.getDosage());
            w.setTiming(m.getTiming());
            w.setStatus(m.getStatus()); // original status (ACTIVE)

            // Initialize new MedicationWithStatus fields
            w.setTaken(false);
            w.setTakenStatus(MedicationWithStatus.DoseStatus.PENDING);
            w.setDoseTime(null);
            w.setLogCreatedAt(null);

            result.add(w);
        }

        return result;
    }

}
