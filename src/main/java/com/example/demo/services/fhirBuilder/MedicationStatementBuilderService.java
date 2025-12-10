package com.example.demo.services.fhirBuilder;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.example.demo.addMedication.repo.*;
import com.example.demo.entity.codeableConcept.CodeSystem;
import com.example.demo.entity.codeableConcept.ConceptCodeMapper;
import com.example.demo.entity.codeableConcept.Concepts;
import com.example.demo.entity.medications.*;
import com.example.demo.repository.codeableConcept.CodeSystemRepo;
import com.example.demo.repository.codeableConcept.ConceptCodeMapperRepo;
import com.example.demo.repository.codeableConcept.ConceptRepo;
import com.example.demo.repository.medications.MedicationsRepo;
import jakarta.transaction.Transactional;
import org.hl7.fhir.r4.model.*;
import org.hl7.fhir.r4.model.Timing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class MedicationStatementBuilderService {
    @Autowired
    MedicationsRepo medicationsRepo;
    @Autowired
    ConceptCodeMapperRepo conceptCodeMapperRepo;
    @Autowired
    CodeSystemRepo codeSystemRepo;
    @Autowired
    ConceptRepo conceptRepo;
    @Autowired
    DosageRepo dosageRepo;
    @Autowired
    TimingRepo timingRepo;
    @Autowired
    SuperPrescriptionRepo superPrescriptionRepo;
    @Autowired
    MedicationStatementRepo medicationStatementRepo;

    @Autowired
    MedicationFhirBuilderService medFhirService;

    @Autowired
    MedicationFhirBuilderService medicationFhirBuilderService;
    @Autowired
    PrescriptionRepo prescriptionRepo;
    private final FhirContext fhirContext = FhirContext.forR4();
    @Transactional
    public void addMedicationStatementFhir(){
        List<MedicationStatements> medStatementWithoutFhir= medicationStatementRepo.findByFhirJsonIsNull();
        System.out.println(medStatementWithoutFhir);
        for (MedicationStatements med : medStatementWithoutFhir) {
            String fhirJson = buildMedication(med);
            med.setFhirJson(fhirJson);
            System.out.println(fhirJson );
        }
    }

    public String buildMedication(MedicationStatements medStatement) {


        MedicationStatement ms = new MedicationStatement();
        //Medicaion id
        ms.setId(String.valueOf(medStatement.getStatementId()));

        Prescription prescription = prescriptionRepo.findById(medStatement.getPrescriptionId())
                .orElseThrow(() -> new RuntimeException("Prescription does not exists"));
        SuperPrescription superPrescription = superPrescriptionRepo.findById(prescription.getSuperPrescriptionId())
                .orElseThrow(()->new RuntimeException("Super Prescription does not exists"));
        //Set patient Id
        ms.setSubject(new Reference("Patient/" + superPrescription.getPatientId()));

        //Set the reason Code
        ConceptCodeMapper reasonCodeMap = conceptCodeMapperRepo.findByConceptId(prescription.getReasonId())
                .orElseThrow(() -> new RuntimeException(String.valueOf(prescription.getReasonId())));
        CodeSystem reasonCodeSystem = codeSystemRepo.findById(reasonCodeMap.getSystemId())
                .orElseThrow(() -> new RuntimeException("CodeSystem not found"));

        String reasonText = reasonCodeMap.getDisplayText();
        CodeableConcept reason = medFhirService.buildCodeableConcept(reasonText, reasonCodeSystem, reasonCodeMap);
        ms.setReasonCode(List.of(reason));

        //Contained data of the medication
        Medications med =  medicationsRepo.findById(medStatement.getMedicationId())
                .orElseThrow(() -> new RuntimeException("Medication does not exists"));
        String medicationJson= med.getFhirJson();

        if (medicationJson== null) {
            medicationFhirBuilderService.MedicationFhirAdd();
            medicationJson = med.getFhirJson();
        }

        IParser parser = fhirContext.newJsonParser();

        Medication medication = parser.parseResource(Medication.class, medicationJson);

        String containedId = "med" + med.getMedicationId();     // ex: "med309"
        medication.setId(containedId);
        ms.getContained().add(medication);
        ms.setMedication(new Reference("#" + containedId));
        //status
        String dbStatus = medStatement
                .getStatus();

        MedicationStatement.MedicationStatementStatus statusEnum =
                MedicationStatement.MedicationStatementStatus.fromCode(dbStatus.toLowerCase());
        ms.setStatus(statusEnum);


        //note
        if (medStatement.getNotes() != null) {
            String notes = medStatement.getNotes();

            Annotation annotation = new Annotation();
            annotation.setText(notes);

            ms.setNote(List.of(annotation)); // FHIR expects List<Annotation>
        }
        Date start = medStatement.getEffectiveStartDate();
        Date end   = medStatement.getEffectiveEndDate();

        if (start != null || end != null) {
            ms.setEffective(new Period().setStart(start).setEnd(end));
        }

        //dosage and timing

        Dosages dosages = dosageRepo.findById(medStatement.getDosageId())
                .orElseThrow(()-> new RuntimeException("DOsage does not exists"));
        if(dosages != null){
            Dosage dosageComponent = new Dosage();
            if (dosages.getAmount() != null && dosages.getAmountUnitId() != null) {
                Dosage.DosageDoseAndRateComponent doseAndRate = new Dosage.DosageDoseAndRateComponent();
                ConceptCodeMapper amountCodeMap = conceptCodeMapperRepo.findByConceptId(dosages.getAmountUnitId())
                        .orElseThrow(() -> new RuntimeException(String.valueOf(dosages.getAmountUnitId())));
                CodeSystem amountCodeSystem = codeSystemRepo.findById(amountCodeMap.getSystemId())
                        .orElseThrow(() -> new RuntimeException("CodeSystem not found"));

                Quantity doseQuantity = new Quantity()
                        .setValue(dosages.getAmount())
                        .setUnit(amountCodeMap.getDisplayText())       // human-readable
                        .setSystem(amountCodeSystem.getSystemName())      // e.g., http://unitsofmeasure.org
                        .setCode(amountCodeMap.getCode());         // e.g., mg, mL

                // 3. Set it in the dosage component
                doseAndRate.setDose(doseQuantity);
                dosageComponent.addDoseAndRate(doseAndRate);

            }

            if(dosages.getRouteId() != null){
                ConceptCodeMapper routeCodeMap = conceptCodeMapperRepo.findByConceptId(dosages.getRouteId())
                        .orElseThrow(() -> new RuntimeException(String.valueOf(dosages.getRouteId())));
                CodeSystem routeCodeSystem = codeSystemRepo.findById(routeCodeMap.getSystemId())
                        .orElseThrow(() -> new RuntimeException("CodeSystem not found"));

                String routeText = routeCodeMap.getDisplayText();
                CodeableConcept route = medFhirService.buildCodeableConcept(routeText, routeCodeSystem, routeCodeMap);
                dosageComponent.setRoute(route);
            }
            if(dosages.getTimingId() != null){
                com.example.demo.entity.medications.Timing timings =  timingRepo.findById(dosages.getTimingId())
                        .orElseThrow(()->new RuntimeException("Timing does nto exist"));
                if (timings != null) {
                    Timing timing = new Timing();

                    // Frequency / period
                    if (timings.getFrequency() != null) {
                        timing.setRepeat(new Timing.TimingRepeatComponent().setFrequency(timings.getFrequency()));
                    }
                    if (timings.getPeriod() != null && timings.getPeriodUnitId() != null) {
                        timing.getRepeat().setPeriod(timings.getPeriod().doubleValue());
                        // Optionally set unit from periodUnitId
                    }

                    // Time of day
                    if (timings.getTimeOfDay() != null) {
                        ConceptCodeMapper whenCodeMap = conceptCodeMapperRepo.findByConceptId(timings.getWhenCodeId())
                                .orElseThrow(() -> new RuntimeException(String.valueOf(timings.getWhenCodeId())));

                        String whenCode =whenCodeMap.getCode();
                        Timing.EventTiming whenEnum =
                                Timing.EventTiming.fromCode(whenCode);

                        timing.getRepeat().addWhen(whenEnum); // example mapping
                    }

                    dosageComponent.setTiming(timing);
                }
            }
            if (dosages.getInstruction() != null) {
                dosageComponent.setPatientInstruction(dosages.getInstruction());

            }
            ms.addDosage(dosageComponent);
        }


        return fhirContext.newJsonParser().setPrettyPrint(true).encodeResourceToString(ms);

    }

}
