package com.example.demo.addMedication.services;

import com.example.demo.addMedication.dto.*;
import com.example.demo.addMedication.repo.*;
import com.example.demo.entity.codeableConcept.Concepts;
import com.example.demo.entity.medications.*;
import com.example.demo.repository.codeableConcept.ConceptRepo;
import com.example.demo.repository.medications.MedicationsRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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
    private MedicationsRepo medicationsRepo;
    @Autowired
    private MedicalConditionService medicalConditionService;
    @Autowired
    private ConceptRepo conceptRepo;
    @Autowired
    private SuperPrescriptionRepo superPrescriptionRepo;
    @Transactional
    public void savePrescription(SuperPrescriptionDto request) {

        // 1️⃣ Save SuperPrescription first
        SuperPrescription superPrescription = new SuperPrescription();
        superPrescription.setPrescriptionDate(request.getPrescriptionDate());
        superPrescription.setDoctorName(request.getDoctorName());
        superPrescription.setCreatedAt(LocalDateTime.now());
        superPrescription.setUpdatedAt(LocalDateTime.now());
        superPrescription.setNotes(request.getNotes());
        superPrescription.setPatientId(request.getPatientId()); // make sure patientId is set

        // ✅ Save to DB to get generated ID
        superPrescription = superPrescriptionRepo.save(superPrescription);

        // 2️⃣ Save all child Prescriptions
        for (PrescriptionDto presReq : request.getPrescriptions()) {

            Prescription prescription = new Prescription();
            long reasonConceptId = getConditionID(presReq.getConditionName());
            prescription.setReasonId(reasonConceptId);
            prescription.setNotes(presReq.getNotes());

            // ✅ Set parent foreign key AFTER saving SuperPrescription
            prescription.setSuperPrescriptionId(superPrescription.getSuperPrescriptionId());

            // Save Prescription
            prescription = prescriptionRepo.save(prescription);

            // 3️⃣ Save MedicationStatements for this Prescription
            for (MedicationStatementDto msReq : presReq.getMedications()) {

                // Save Timing
                Timing timing = null;
                if (msReq.getTiming() != null) {
                    TimingDto t = msReq.getTiming();
                    timing = new Timing();
                    timing.setFrequency(t.getFrequency());
                    timing.setPeriod(t.getPeriod());
                    long periodUnitId = getConceptId(t.getPeriodUnit(), "Units");
                    timing.setPeriodUnitId(periodUnitId);
                    if (t.getTimeOfDay() != null)
                        timing.setTimeOfDay(Time.valueOf(t.getTimeOfDay().toLocalTime()));
                    long whenCode = getConceptId(t.getWhenCode(), "When-Timing");
                    timing.setWhenCodeId(whenCode);

                    timing = timingRepo.save(timing);
                }

                // Save Dosage
                Dosages dosage = null;
                if (msReq.getDosage() != null) {
                    DosageRequestDto d = msReq.getDosage();
                    dosage = new Dosages();
                    dosage.setAmount(d.getAmount());
                    dosage.setAmountUnitId(d.getAmountUnitId());
                    dosage.setRouteId(d.getRouteId());
                    dosage.setInstruction(d.getInstruction());
                    if (timing != null)
                        dosage.setTimingId(timing.getTimingId());

                    dosage = dosageRepo.save(dosage);
                }

                // Save MedicationStatement
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
    }
    @Transactional
    public void updatePrescription(Long superPrescriptionId, SuperPrescriptionResponseDto request) {
        // 1️⃣ Fetch existing SuperPrescription
        SuperPrescription superPrescription = superPrescriptionRepo.findById(superPrescriptionId)
                .orElseThrow(() -> new RuntimeException("SuperPrescription not found"));

        // Update fields
        superPrescription.setPrescriptionDate(request.getPrescriptionDate());
        superPrescription.setDoctorName(request.getDoctorName());
        superPrescription.setUpdatedAt(LocalDateTime.now());
        superPrescription.setNotes(request.getNotes());
        superPrescription.setPatientId(request.getPatientId());

        superPrescriptionRepo.save(superPrescription);

        // 2️⃣ Update child Prescriptions
        for (PrescriptionResponseDto presReq : request.getPrescriptions()) {
            Prescription prescription;
            if (presReq.getPrescriptionId() != null) {
                // Update existing prescription
                prescription = prescriptionRepo.findById(presReq.getPrescriptionId())
                        .orElseThrow(() -> new RuntimeException("Prescription not found"));
            } else {
                // New prescription
                prescription = new Prescription();
                prescription.setSuperPrescriptionId(superPrescription.getSuperPrescriptionId());
            }

            prescription.setReasonId(getConditionID(presReq.getConditionName()));
            prescription.setNotes(presReq.getNotes());
            prescription = prescriptionRepo.save(prescription);

            // 3️⃣ Update / Save MedicationStatements
            for (MedicationResponseDto msReq : presReq.getMedications()) {

                // Timing
                Timing timing = null;
                if (msReq.getTiming() != null) {
                    TimingResponseDto t = msReq.getTiming();
                    if (t.getTimingId() != null) {
                        timing = timingRepo.findById(t.getTimingId())
                                .orElse(new Timing());
                    } else {
                        timing = new Timing();
                    }
                    timing.setFrequency(t.getFrequency());
                    timing.setPeriod(t.getPeriod());
                    timing.setPeriodUnitId(getConceptId(t.getPeriodUnit(), "Units"));
                    if (t.getTimeOfDay() != null)
                        timing.setTimeOfDay(Time.valueOf(t.getTimeOfDay().toLocalTime()));
                    timing.setWhenCodeId(getConceptId(t.getWhenCode(), "When-Timing"));
                    timing = timingRepo.save(timing);
                }

                // Dosage
                Dosages dosage = null;
                if (msReq.getDosage() != null) {
                    DosageResponseDto d = msReq.getDosage();
                    if (d.getDosageId() != null) {
                        dosage = dosageRepo.findById(d.getDosageId()).orElse(new Dosages());
                    } else {
                        dosage = new Dosages();
                    }
                    dosage.setAmount(d.getAmount());
                    dosage.setAmountUnitId(d.getAmountUnitId());
                    dosage.setRouteId(d.getRouteId());
                    dosage.setInstruction(d.getInstruction());
                    if (timing != null) dosage.setTimingId(timing.getTimingId());
                    dosage = dosageRepo.save(dosage);
                }

                // MedicationStatement
                MedicationStatements ms;
                if (msReq.getStatementId() != null) {
                    ms = medicationStatementRepo.findById(msReq.getStatementId())
                            .orElse(new MedicationStatements());
                } else {
                    ms = new MedicationStatements();
                    ms.setPrescriptionId(prescription.getPrescriptionId());
                }

                ms.setMedicationId(msReq.getMedicationId());
                ms.setEffectiveStartDate(msReq.getEffectiveStartDate());
                ms.setEffectiveEndDate(msReq.getEffectiveEndDate());
                ms.setDosageId(dosage != null ? dosage.getDosageId() : null);
                ms.setStatus(msReq.getStatus());
                ms.setFhirJson(null);

                medicationStatementRepo.save(ms);
            }
        }
    }

    public List<SuperPrescriptionResponseDto> getSuperPrescriptionByPatient(Long patientId) {
        List<SuperPrescription> superPrescriptions = superPrescriptionRepo.findByPatientId(patientId);
        return superPrescriptions.stream().map(superPrescription -> {
            SuperPrescriptionResponseDto spdto = new SuperPrescriptionResponseDto();
            spdto.setSuperPrescriptionId(superPrescription.getSuperPrescriptionId());
            spdto.setPatientId(superPrescription.getPatientId());
            spdto.setDoctorName(superPrescription.getDoctorName());
            spdto.setNotes(superPrescription.getNotes());
            spdto.setPrescriptionDate(superPrescription.getPrescriptionDate());

            //Prescription Dto
            List<Prescription> prescriptions = prescriptionRepo.findBySuperPrescriptionId(superPrescription.getSuperPrescriptionId());

            List<PrescriptionResponseDto> prescriptionDTOs = prescriptions.stream().map(prescription -> {
                PrescriptionResponseDto pdto = new PrescriptionResponseDto();

                pdto.setNotes(prescription.getNotes());
                pdto.setPrescriptionId(prescription.getPrescriptionId());
//            // Convert conditionId → condition name
                Concepts condition = conceptRepo.findById(prescription.getReasonId())
                        .orElse(null);
                pdto.setConditionName(condition != null ? condition.getConceptName() : null);
//                pdto.setConditionId(condition != null ? condition.getConceptId() : null);
                List<MedicationStatements> statements =
                        medicationStatementRepo.findByPrescriptionId(prescription.getPrescriptionId());

                List<MedicationResponseDto> medicationDTOs = statements.stream().map(ms -> {

                    MedicationResponseDto msDto = new MedicationResponseDto();
                    msDto.setStatementId(ms.getStatementId());
                    Medications medicine = medicationsRepo.findById(ms.getMedicationId())
                            .orElse(null);
                    msDto.setMedication(medicine != null ? medicine.getBrandName() : null);
                    msDto.setMedicationId(medicine != null ? medicine.getMedicationId() : null);

                    msDto.setStatus(ms.getStatus());
                    msDto.setEffectiveStartDate(ms.getEffectiveStartDate());
                    msDto.setEffectiveEndDate(ms.getEffectiveEndDate());

                    // Fetch dosage
                    if (ms.getDosageId() != null) {
                        Dosages d = dosageRepo.findById(ms.getDosageId()).orElse(null);
                        if (d != null) {
                            DosageResponseDto dosageDto = new DosageResponseDto();
                            dosageDto.setAmount(d.getAmount());
                            dosageDto.setDosageId(d.getDosageId());
                            Concepts amount = conceptRepo.findById(d.getAmountUnitId())
                                    .orElse(null);
                            dosageDto.setAmountUnit(amount != null ? amount.getConceptName() : null);
                            dosageDto.setAmountUnitId(amount != null ? amount.getConceptId() : null);
                            Concepts route = conceptRepo.findById(d.getRouteId())
                                    .orElse(null);
                            dosageDto.setRoute(route != null ?route.getConceptName() : null);
                            dosageDto.setRouteId(route != null ?route.getConceptId() : null);

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
                                TimingResponseDto timingDto = new TimingResponseDto();
                                timingDto.setTimingId(t.getTimingId());
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

                pdto.setMedications(medicationDTOs);

                return pdto;
            }).toList();
            spdto.setPrescriptions(prescriptionDTOs);
            return spdto;

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

    public Optional<SuperPrescriptionResponseDto> getSuperPrescriptionById(Long superPrescriptionId) {
        SuperPrescription superPrescription = superPrescriptionRepo.findById(superPrescriptionId)
                .orElseThrow(() -> new RuntimeException("SuperPrescription not found"));
        SuperPrescriptionResponseDto spdto = new SuperPrescriptionResponseDto();
        spdto.setSuperPrescriptionId(superPrescription.getSuperPrescriptionId());
        spdto.setPatientId(superPrescription.getPatientId());
        spdto.setDoctorName(superPrescription.getDoctorName());
        spdto.setNotes(superPrescription.getNotes());
        spdto.setPrescriptionDate(superPrescription.getPrescriptionDate());

        //Prescription Dto
        List<Prescription> prescriptions = prescriptionRepo.findBySuperPrescriptionId(superPrescription.getSuperPrescriptionId());

        List<PrescriptionResponseDto> prescriptionDTOs = prescriptions.stream().map(prescription -> {
            PrescriptionResponseDto pdto = new PrescriptionResponseDto();

            pdto.setNotes(prescription.getNotes());
            pdto.setPrescriptionId(prescription.getPrescriptionId());
//            // Convert conditionId → condition name
            Concepts condition = conceptRepo.findById(prescription.getReasonId())
                    .orElse(null);
            pdto.setConditionName(condition != null ? condition.getConceptName() : null);
//                pdto.setConditionId(condition != null ? condition.getConceptId() : null);
            List<MedicationStatements> statements =
                    medicationStatementRepo.findByPrescriptionId(prescription.getPrescriptionId());

            List<MedicationResponseDto> medicationDTOs = statements.stream().map(ms -> {

                MedicationResponseDto msDto = new MedicationResponseDto();
                msDto.setStatementId(ms.getStatementId());
                Medications medicine = medicationsRepo.findById(ms.getMedicationId())
                        .orElse(null);
                msDto.setMedication(medicine != null ? medicine.getBrandName() : null);
                msDto.setMedicationId(medicine != null ? medicine.getMedicationId() : null);

                msDto.setStatus(ms.getStatus());
                msDto.setEffectiveStartDate(ms.getEffectiveStartDate());
                msDto.setEffectiveEndDate(ms.getEffectiveEndDate());

                // Fetch dosage
                if (ms.getDosageId() != null) {
                    Dosages d = dosageRepo.findById(ms.getDosageId()).orElse(null);
                    if (d != null) {
                        DosageResponseDto dosageDto = new DosageResponseDto();
                        dosageDto.setAmount(d.getAmount());
                        dosageDto.setDosageId(d.getDosageId());
                        Concepts amount = conceptRepo.findById(d.getAmountUnitId())
                                .orElse(null);
                        dosageDto.setAmountUnit(amount != null ? amount.getConceptName() : null);
                        dosageDto.setAmountUnitId(amount != null ? amount.getConceptId() : null);
                        Concepts route = conceptRepo.findById(d.getRouteId())
                                .orElse(null);
                        dosageDto.setRoute(route != null ?route.getConceptName() : null);
                        dosageDto.setRouteId(route != null ?route.getConceptId() : null);

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
                            TimingResponseDto timingDto = new TimingResponseDto();
                            timingDto.setTimingId(t.getTimingId());
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

            pdto.setMedications(medicationDTOs);

            return pdto;
        }).toList();
        spdto.setPrescriptions(prescriptionDTOs);
        return Optional.of(spdto);
    }
}