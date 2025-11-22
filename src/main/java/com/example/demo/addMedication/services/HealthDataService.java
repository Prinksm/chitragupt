package com.example.demo.addMedication.services;

import com.example.demo.addMedication.dto.DosageRequestDto;
import com.example.demo.addMedication.dto.MedicationStatementDto;
import com.example.demo.addMedication.dto.PrescriptionDto;
import com.example.demo.addMedication.dto.TimingDto;
import com.example.demo.addMedication.repo.DosageRepo;
import com.example.demo.addMedication.repo.MedicationStatementRepo;
import com.example.demo.addMedication.repo.PrescriptionRepo;
import com.example.demo.addMedication.repo.TimingRepo;
import com.example.demo.entity.codeableConcept.Concepts;
import com.example.demo.entity.medications.Dosages;
import com.example.demo.entity.medications.MedicationStatements;
import com.example.demo.entity.medications.Prescription;
import com.example.demo.entity.medications.Timing;
import com.example.demo.repository.codeableConcept.ConceptRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;

@Service
public class HealthDataService {
    @Autowired
    private PrescriptionRepo prescriptionRepo;
    @Autowired
    private MedicationStatementRepo medicationStatementRepo;
    @Autowired
    private DosageRepo dosageRepo;
    @Autowired
    private TimingRepo timingRepo;
    @Autowired
    private MedicalConditionService medicalConditionService;
    @Autowired
    private ConceptRepo conceptRepo;

    @Transactional
    public void savePrescription(PrescriptionDto request) {

//        // 1️⃣ Save Prescription
        Prescription prescription = new Prescription();
        prescription.setPatientId(request.getPatientId());

        long reasonConceptId = getConditionID(request.getConditionName());
        prescription.setReasonId(reasonConceptId);
        prescription.setNotes(request.getNotes());
        prescription = prescriptionRepo.save(prescription);
//
//        // 2️⃣ Loop through medications
        for (MedicationStatementDto msReq : request.getMedications()) {
            System.out.println(msReq.getTiming());
//            // Save Timing
            Timing timing = new Timing();
            if (msReq.getTiming() != null) {
                TimingDto t = msReq.getTiming();
                timing.setFrequency(t.getFrequency());
                timing.setPeriod(t.getPeriod());
                long periodUnitId = getConceptId(t.getPeriodUnit(), "Units");
                timing.setPeriodUnitId(periodUnitId);
                if (t.getTimeOfDay() != null) timing.setTimeOfDay(Time.valueOf(t.getTimeOfDay().toLocalTime()));
                long whenCode = getConceptId(t.getWhenCode(), "When-Timing");
                timing.setWhenCodeId(whenCode);

                timing = timingRepo.save(timing);
            }
//
//            // Save Dosage
            Dosages dosage = new Dosages();
            if (msReq.getDosage() != null) {
                DosageRequestDto d = msReq.getDosage();
                dosage.setAmount(d.getAmount());
                dosage.setAmountUnitId(d.getAmountUnitId());
                dosage.setRouteId(d.getRouteId());
                dosage.setInstruction(d.getInstruction());
                if (timing != null) dosage.setTimingId(timing.getTimingId());
                dosage = dosageRepo.save(dosage);
            }
//
//            // Save MedicationStatement
            MedicationStatements ms = new MedicationStatements();
            ms.setPrescriptionId(prescription.getPrescriptionId());
            ms.setMedicationId(msReq.getMedicationId());
            ms.setEffectiveStartDate(msReq.getEffectiveStartDate());
            ms.setEffectiveEndDate(msReq.getEffectiveEndDate());
            ms.setDosageId(dosage != null ? dosage.getDosageId() : null);
            ms.setStatus(msReq.getStatus());
            medicationStatementRepo.save(ms);
        }
    }
    public List<PrescriptionDto> getPrescriptionsByPatient(Long patientId) {

        List<Prescription> prescriptions = prescriptionRepo.findByPatientId(patientId);

        return prescriptions.stream().map(prescription -> {

            PrescriptionDto dto = new PrescriptionDto();
            dto.setPatientId(prescription.getPatientId());
            dto.setNotes(prescription.getNotes());

            // Convert conditionId → condition name
            Concepts condition = conceptRepo.findById(prescription.getReasonId())
                    .orElse(null);
            dto.setConditionName(condition != null ? condition.getConceptName() : null);

            // Fetch medications inside prescription
            List<MedicationStatements> statements =
                    medicationStatementRepo.findByPrescriptionId(prescription.getPrescriptionId());

            List<MedicationStatementDto> medicationDTOs = statements.stream().map(ms -> {

                MedicationStatementDto msDto = new MedicationStatementDto();
                msDto.setMedicationId(ms.getMedicationId());
                msDto.setStatus(ms.getStatus());
                msDto.setEffectiveStartDate(ms.getEffectiveStartDate());
                msDto.setEffectiveEndDate(ms.getEffectiveEndDate());

                // Fetch dosage
                if (ms.getDosageId() != null) {
                    Dosages d = dosageRepo.findById(ms.getDosageId()).orElse(null);
                    if (d != null) {
                        DosageRequestDto dosageDto = new DosageRequestDto();
                        dosageDto.setAmount(d.getAmount());
                        dosageDto.setAmountUnitId(d.getAmountUnitId());
                        dosageDto.setRouteId(d.getRouteId());
                        dosageDto.setInstruction(d.getInstruction());
                        msDto.setDosage(dosageDto);
                    }
                }

                // Fetch timing
                if (ms.getDosageId() != null) {
                    Dosages d = dosageRepo.findById(ms.getDosageId()).orElse(null);
                    if (d != null && d.getTimingId() != null) {
                        Timing t = timingRepo.findById(d.getTimingId()).orElse(null);
                        if (t != null) {
                            TimingDto timingDto = new TimingDto();
                            timingDto.setFrequency(t.getFrequency());
                            timingDto.setPeriod(t.getPeriod());

                            Concepts pu = conceptRepo.findById(t.getPeriodUnitId()).orElse(null);
                            timingDto.setPeriodUnit(pu != null ? pu.getConceptName() : null);

                            timingDto.setTimeOfDay(t.getTimeOfDay());

                            Concepts when = conceptRepo.findById(t.getWhenCodeId()).orElse(null);
                            timingDto.setWhenCode(when != null ? when.getConceptName() : null);

                            msDto.setTiming(timingDto);
                        }
                    }
                }

                return msDto;
            }).toList();

            dto.setMedications(medicationDTOs);

            return dto;
        }).toList();
    }


    public long getConceptId(String conceptName, String type) {
        return conceptRepo.findByConceptNameAndType(conceptName, type)
                .map(Concepts::getConceptId)
                .orElseThrow(() ->
                        new RuntimeException("Concept not found: " + conceptName + " (" + type + ")"));
    }

    public long getConditionID(String conceptName) {

        Concepts concept = conceptRepo.findByConceptNameAndType(conceptName, "Medical-condition")
                .orElseGet(() -> medicalConditionService.fetchConditionAndSave(conceptName));

        return concept.getConceptId();
    }

}