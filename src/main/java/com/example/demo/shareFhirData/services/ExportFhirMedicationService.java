package com.example.demo.shareFhirData.services;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.example.demo.addMedication.repo.MedicationStatementRepo;
import com.example.demo.addMedication.repo.PrescriptionRepo;
import com.example.demo.entity.medications.MedicationStatements;
import com.example.demo.entity.medications.Prescription;
import com.example.demo.entity.patientEntity.Patient;
import com.example.demo.profile.repository.PatientRepository;
import com.example.demo.services.fhirBuilder.MedicationStatementBuilderService;
import com.example.demo.services.fhirBuilder.PatientFhirBuilderService;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.MedicationStatement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExportFhirMedicationService {

    @Autowired
    private MedicationStatementRepo medicationStatementRepo;

    @Autowired
    private PrescriptionRepo prescriptionRepo;
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private MedicationStatementBuilderService medicationStatementBuilderService;
    @Autowired
    private PatientFhirBuilderService patientFhirBuilderService;

    private final FhirContext fhirContext = FhirContext.forR4();

    public String buildMedicationBundle(List<Long> superPrescriptionIds) {

        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.COLLECTION);

        IParser parser = fhirContext.newJsonParser();

        for (Long superId : superPrescriptionIds) {

            List<Prescription> prescriptions =
                    prescriptionRepo.findBySuperPrescriptionId(superId);


            for (Prescription prescription : prescriptions) {

                List<MedicationStatements> medStatements =
                        medicationStatementRepo.findByPrescriptionId(prescription.getPrescriptionId());
                for (MedicationStatements medStmt : medStatements) {
                    String medJson = medStmt.getFhirJson();

                    if (medJson == null) {
                        medicationStatementBuilderService.addMedicationStatementFhir();
                        medJson = medStmt.getFhirJson();
                    }


                    MedicationStatement ms =
                            parser.parseResource(MedicationStatement.class, medJson);

                    bundle.addEntry()
                            .setResource(ms);
                }
            }
        }


        return parser
                .setPrettyPrint(true)
                .encodeResourceToString(bundle);
    }

    public String buildPatientBundle(Long PatientId) {
        Bundle bundle = new Bundle();
        bundle.setType(Bundle.BundleType.COLLECTION);


        IParser parser = fhirContext.newJsonParser();
        Patient patient = patientRepository.findById(PatientId).
                orElseThrow(() -> new RuntimeException("Patient does not exist"));
        patient.setFhir(null);
        patientFhirBuilderService.buildPatientFhirJson();
        String patientJson = patient.getFhir();

        org.hl7.fhir.r4.model.Patient ms =
                parser.parseResource(org.hl7.fhir.r4.model.Patient.class, patientJson);

        bundle.addEntry()
                .setResource(ms);
        return parser
                .setPrettyPrint(true)
                .encodeResourceToString(bundle);
    }
}
