package com.example.demo.services.fhirBuilder;

import ca.uhn.fhir.context.FhirContext;
import com.example.demo.entity.codeableConcept.CodeSystem;
import com.example.demo.entity.codeableConcept.ConceptCodeMapper;
import com.example.demo.entity.codeableConcept.Concepts;
import com.example.demo.entity.medications.*;
import com.example.demo.repository.codeableConcept.CodeSystemRepo;
import com.example.demo.repository.codeableConcept.ConceptCodeMapperRepo;
import com.example.demo.repository.codeableConcept.ConceptRepo;
import com.example.demo.repository.medications.*;
import jakarta.transaction.Transactional;
import org.hl7.fhir.r4.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicationFhirBuilderService {

    @Autowired
    MedicationsRepo medicationsRepo;
    @Autowired
    ConceptCodeMapperRepo conceptCodeMapperRepo;
    @Autowired
    CodeSystemRepo codeSystemRepo;
    @Autowired
    ConceptRepo conceptRepo;
    @Autowired
    StrengthRepo strengthRepo;

    @Autowired
    IngredientRepo ingredientRepo;
    @Autowired
    DoseFormRepo doseFormRepo;
    @Autowired
    MedicineIngredientMapperRepo medicineIngredientMapperRepo;
    @Autowired
    MedicineDoseMapperRepo medicineDoseMapperRepo;

    @Transactional
    public void MedicationFhirAdd() {
//Check the medication table
        List<Medications> medsWithoutFhir = medicationsRepo.findByFhirJsonIsNull();
        System.out.println(medsWithoutFhir);
        for (Medications med : medsWithoutFhir) {
            String fhirJson = buildMedication(med);
            med.setFhirJson(fhirJson);
            System.out.println(fhirJson + " " + med.getBrandName());
        }


    }

    public String buildMedication(Medications med) {

        Medication fhirMed = new Medication();
        //Medicaion id
        fhirMed.setId(String.valueOf(med.getMedicationId()));
        //Medication code
        ConceptCodeMapper mapping = conceptCodeMapperRepo.findByConceptId(med.getConceptId())
                .orElseThrow(() -> new RuntimeException(String.valueOf(med.getConceptId())));
        CodeSystem codeSystem = codeSystemRepo.findById(mapping.getSystemId())
                .orElseThrow(() -> new RuntimeException("CodeSystem not found"));
        CodeableConcept medicationCode = buildCodeableConcept(med.getBrandName(), codeSystem, mapping);
        fhirMed.setCode(medicationCode);
        //Medication dose form
        MedicationDoseMapper doseMap = medicineDoseMapperRepo.findByMedicationId(med.getMedicationId())
                .orElseThrow(() -> new RuntimeException("Medication dose form map does not exists"));
        DoseForms doseForms = doseFormRepo.findById(doseMap.getDoseformId()).orElseThrow(() -> new RuntimeException("Dose form id does not exists"));

        ConceptCodeMapper doseCodeMap = conceptCodeMapperRepo.findByConceptId(doseForms.getConceptId())
                .orElseThrow(() -> new RuntimeException(String.valueOf(doseForms.getConceptId())));
        CodeSystem doseCodeSystem = codeSystemRepo.findById(doseCodeMap.getSystemId())
                .orElseThrow(() -> new RuntimeException("CodeSystem not found"));
//        CodeableConcept doseCode = buildCodeableConcept(med.getBrandName(), codeSystem, mapping);
        String doseText = doseCodeMap.getDisplayText();

        CodeableConcept doseform = buildCodeableConcept(doseText, doseCodeSystem, doseCodeMap);

        fhirMed.setForm(doseform);

        //Medication Ingredient
        List<MedicationIngredientMapper> ingredients = medicineIngredientMapperRepo.findByMedicationId(med.getMedicationId());
        for (MedicationIngredientMapper ingredient : ingredients) {
            //Ingredient Component creation

            //Fetching the ingredient
            Ingredients ing = ingredientRepo.findById(ingredient.getIngredientId())
                    .orElseThrow(() -> new RuntimeException("Ingredient does not exist"));

            System.out.println("Ingredient ID: " + ing.getIngredientId() + ing.getConceptId());

            ConceptCodeMapper ingMap = conceptCodeMapperRepo.findByConceptId(ing.getConceptId())
                    .orElseThrow(() -> new RuntimeException(String.valueOf(ing.getConceptId())));
            Concepts ingConcept = conceptRepo.findById(ing.getConceptId())
                    .orElseThrow(() -> new RuntimeException(String.valueOf(ing.getConceptId())));
            CodeSystem ingCodeSystem = codeSystemRepo.findById(mapping.getSystemId())
                    .orElseThrow(() -> new RuntimeException("CodeSystem not found"));
            CodeableConcept ingredientCode = buildCodeableConcept(ingConcept.getConceptName(), ingCodeSystem, ingMap);


            Medication.MedicationIngredientComponent ingredientComponent =
                    new Medication.MedicationIngredientComponent();


            // Attach the Ratio to "strength"
            Strengths ingStrength = strengthRepo.findById(ingredient.getStrengthId())
                    .orElseThrow(() -> new RuntimeException("Stength does not exists"));
            //Unit code and system fetching
            ConceptCodeMapper unitMap = conceptCodeMapperRepo.findByConceptId(ingStrength.getUnitId())
                    .orElseThrow(() -> new RuntimeException("Unit concept does not exists"));
            Concepts unitConcept = conceptRepo.findById(unitMap.getConceptId())
                    .orElseThrow(() -> new RuntimeException("Concept name does not exists"));
            CodeSystem unitSystem = codeSystemRepo.findById(unitMap.getSystemId())
                    .orElseThrow(() -> new RuntimeException("Unit system does not exists"));


            Quantity numerator = new Quantity()
                    .setValue(ingStrength.getValue())
                    .setSystem(unitSystem.getSystemName())
                    .setCode(unitMap.getCode());
            Ratio strengthRatio = new Ratio()
                    .setNumerator(numerator);
            ingredientComponent.setItem(ingredientCode);
            ingredientComponent.setStrength(strengthRatio);
            fhirMed.addIngredient(ingredientComponent);


        }


        FhirContext ctx = FhirContext.forR4();
        return ctx.newJsonParser().setPrettyPrint(true).encodeResourceToString(fhirMed);

    }

    public CodeableConcept buildCodeableConcept(String displayText, CodeSystem codeSystem, ConceptCodeMapper mapping) {
        CodeableConcept concept = new CodeableConcept();

        if (codeSystem != null && mapping != null) {
            Coding coding = new Coding()
                    .setSystem(codeSystem.getSystemName())   // e.g. "http://www.nlm.nih.gov/research/umls/rxnorm"
                    .setCode(mapping.getCode())              // e.g. "12345"
                    .setDisplay(displayText);                // e.g. "Paracetamol 500mg Tablet"

            concept.addCoding(coding);
        }


        concept.setText(displayText);

        return concept;
    }

}


//public Medication.MedicationIngredientComponent buildMedicationIngredient() {
//    // 1️⃣ Create Coding for the ingredient (concept)
//    Coding ingredientCoding = new Coding()
//            .setSystem("http://snomed.info/sct")
//            .setCode("387138002")
//            .setDisplay("Busulfan (substance)");
//
//    // 2️⃣ Wrap Coding inside CodeableConcept
//    CodeableConcept ingredientConcept = new CodeableConcept()
//            .addCoding(ingredientCoding);
//
//    // 3️⃣ Create Numerator (strength value and unit)
//    Quantity numerator = new Quantity()
//            .setValue(2)
//            .setSystem("http://unitsofmeasure.org")
//            .setCode("mg");
//
//    // 4️⃣ Create Denominator (form, like "tablet")
//    Quantity denominator = new Quantity()
//            .setValue(1)
//            .setSystem("http://terminology.hl7.org/CodeSystem/v3-orderableDrugForm")
//            .setCode("TAB");
//
//    // 5️⃣ Combine into a Ratio
//    Ratio strengthRatio = new Ratio()
//            .setNumerator(numerator)
//            .setDenominator(denominator);
//
//    // 6️⃣ Create Ingredient Component
//    Medication.MedicationIngredientComponent ingredientComponent =
//            new Medication.MedicationIngredientComponent();
//
//    // Attach the CodeableConcept to the "item"
//    ingredientComponent.setItem(ingredientConcept);
//
//    // Attach the Ratio to "strength"
//    ingredientComponent.setStrength(strengthRatio);
//
//    return ingredientComponent;
//}
