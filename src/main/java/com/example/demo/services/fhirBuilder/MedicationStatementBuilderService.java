package com.example.demo.services.fhirBuilder;

import ca.uhn.fhir.context.FhirContext;
import com.example.demo.addMedication.repo.MedicationStatementRepo;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    MedicationStatementRepo medicationStatementRepo;

    @Transactional
    public void addMedicationStatementFhir(){
        List<MedicationStatements> medStatementWithoutFhir= medicationStatementRepo.findByFhirJsonIsNull();
        System.out.println(medStatementWithoutFhir);
        for (MedicationStatements med : medStatementWithoutFhir) {
            String fhirJson = buildMedication(med);
//            med.setFhirJson(fhirJson);
            System.out.println(fhirJson );
        }
    }

    public String buildMedication(MedicationStatements med) {

        MedicationStatement fhirMed = new MedicationStatement();
        //Medicaion id
        fhirMed.setId(String.valueOf(med.getStatementId()));

        //Contained data of the medication
        //Medication Reference
        //Subject Reference patient id and the name of the patient

        //status
        //reason
        //note
        //dosage and timing



        FhirContext ctx = FhirContext.forR4();
        return ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(fhirMed);

    }

}
