package com.example.demo.shareFhirData.services;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.example.demo.addMedication.repo.MedicationStatementRepo;
import com.example.demo.addMedication.repo.PrescriptionRepo;
import com.example.demo.entity.medications.MedicationStatements;
import com.example.demo.entity.medications.Prescription;
import com.example.demo.services.fhirBuilder.MedicationStatementBuilderService;
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
    private MedicationStatementBuilderService medicationStatementBuilderService;

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
}
