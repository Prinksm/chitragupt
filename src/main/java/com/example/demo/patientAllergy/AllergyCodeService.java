package com.example.demo.patientAllergy;

import com.example.demo.addMedication.dto.ConditionDto;
import com.example.demo.entity.codeableConcept.CodeSystem;
import com.example.demo.entity.codeableConcept.ConceptCodeMapper;
import com.example.demo.entity.codeableConcept.Concepts;
import com.example.demo.repository.codeableConcept.CodeSystemRepo;
import com.example.demo.repository.codeableConcept.ConceptCodeMapperRepo;
import com.example.demo.repository.codeableConcept.ConceptRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class AllergyCodeService {
    @Autowired
    private ConceptRepo conceptRepo;
    @Autowired
    private CodeSystemRepo codeSystemRepo;
    @Autowired
    private ConceptCodeMapperRepo conceptCodeMapperRepo;
    private final RestTemplate restTemplate = new RestTemplate();

    public long getConditionID(String conceptName) {

        Concepts concept = conceptRepo.findByConceptNameAndType(conceptName, "Allergy")
                .orElseGet(() -> fetchConditionAndSave(conceptName));

        return concept.getConceptId();
    }

    public Concepts fetchConditionAndSave(String conditionName) {

        try {
            String url = "https://clinicaltables.nlm.nih.gov/api/conditions/v3/search?terms="
                    + conditionName
                    + "&df=primary_name,consumer_name&ef=icd10cm_codes";

            ResponseEntity<Object[]> response = restTemplate.getForEntity(url, Object[].class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Object[] rootList = response.getBody();

                // Extract ICD-10 code
                Map<String, Object> icdMap = (Map<String, Object>) rootList[2];
                List<String> icdCodes = (List<String>) icdMap.get("icd10cm_codes");
                String icdCode = (icdCodes != null && !icdCodes.isEmpty()) ? icdCodes.get(0) : null;

                // Extract names
                List<List<String>> namesArray = (List<List<String>>) rootList[3];
                List<String> firstRow = (namesArray != null && !namesArray.isEmpty()) ? namesArray.get(0) : null;
                String primaryName = (firstRow != null && !firstRow.isEmpty()) ? firstRow.get(0) : null;

                if (primaryName == null || icdCode == null) {
                    throw new RuntimeException("Condition not found or no ICD code available for: " + conditionName);
                }

                // Build DTO
                ConditionDto dto = new ConditionDto();
                dto.setConceptName(primaryName);
                dto.setIcd10Code(icdCode);
                dto.setDescription(primaryName); // can use primaryName or consumerName

                // Save and return concept
                return ConditionConceptId(dto);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch condition: " + conditionName, e);
        }

        throw new RuntimeException("Condition not found: " + conditionName);
    }

    //Store and return the concept id of the Condition
    public Concepts ConditionConceptId(ConditionDto condition) {
        String systemUrl = "ICD-10-CM";
        String display = condition.getConceptName();
        String code = condition.getIcd10Code();
        String description = condition.getDescription();

        CodeSystem system = codeSystemRepo.findBySystemName(systemUrl)
                .orElseGet(() -> {
                    CodeSystem newSystem = new CodeSystem();
                    newSystem.setSystemName(systemUrl);
                    return codeSystemRepo.save(newSystem);
                });

        Concepts concept = conceptRepo.findByConceptNameAndType(display, "Allergy")
                .orElseGet(() -> {
                    Concepts c = new Concepts();
                    c.setConceptName(display);
                    c.setDescription(description);
                    c.setType("Allergy");
                    return conceptRepo.save(c);
                });

        ConceptCodeMapper mapping = new ConceptCodeMapper();
        mapping.setCode(code);
        mapping.setSystemId(system.getSystemId());
        mapping.setConceptId(concept.getConceptId());
        mapping.setDisplayText(concept.getConceptName());

        conceptCodeMapperRepo.save(mapping);
        return concept;
    }
}
