package com.example.demo.services.DataImports;

import com.example.demo.entity.codeableConcept.CodeSystem;
import com.example.demo.entity.codeableConcept.ConceptCodeMapper;
import com.example.demo.entity.codeableConcept.Concepts;
import com.example.demo.entity.medications.DoseForms;
import com.example.demo.repository.codeableConcept.CodeSystemRepo;
import com.example.demo.repository.codeableConcept.ConceptCodeMapperRepo;
import com.example.demo.repository.codeableConcept.ConceptRepo;
import com.example.demo.repository.medications.DoseFormRepo;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DecimalFormat;

@Service
public class DoseFormExcelImportService {
    @Autowired
    CodeSystemRepo codeSystemRepo;
    @Autowired
    ConceptRepo conceptRepo;
    @Autowired
    ConceptCodeMapperRepo conceptCodeMapperRepo;
    @Autowired
    DoseFormRepo doseFormRepo;

    public void importDoseFormExcel(MultipartFile file) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() ==0) continue;
                String systemUrl = row.getCell(0).getStringCellValue().trim();

                double numericValue = row.getCell(1).getNumericCellValue();
                DecimalFormat decimalFormat = new DecimalFormat("#");
                String code = decimalFormat.format(numericValue);

                String display = row.getCell(2).getStringCellValue().trim();
                String description = row.getCell(3).getStringCellValue().trim();
                System.out.println(systemUrl);
                System.out.println(code);
                System.out.println(display);
                CodeSystem system = codeSystemRepo.findBySystemName(systemUrl)
                        .orElseGet(() -> {

                            CodeSystem newSystem = new CodeSystem();
                            newSystem.setSystemName(systemUrl);
                            return codeSystemRepo.save(newSystem);
                        });


                Concepts concept = conceptRepo.findByConceptNameAndType(description, "Dose Form")
                        .orElseGet(() -> {

                            Concepts newConcept = new Concepts();
                            newConcept.setConceptName(description);
                            newConcept.setDescription(display);
                            newConcept.setType("Dose Form");
                            return conceptRepo.save(newConcept);
                        });


                ConceptCodeMapper existingMapping = conceptCodeMapperRepo.findByCode(code)
                        .orElseGet(() -> {

                            ConceptCodeMapper newMapping = new ConceptCodeMapper();
                            newMapping.setCode(code);
                            newMapping.setSystemId(system.getSystemId());
                            newMapping.setConceptId(concept.getConceptId());
                            newMapping.setDisplayText(concept.getConceptName());
                            return conceptCodeMapperRepo.save(newMapping);
                        });


                DoseForms existingDoseForm = doseFormRepo.findByConceptId(concept.getConceptId())
                        .orElseGet(() -> {
                            DoseForms newDoseForm = new DoseForms();
                            newDoseForm.setConceptId(concept.getConceptId());
                            return doseFormRepo.save(newDoseForm);
                        });

                System.out.println("Concept ID: " + concept.getConceptId());
            }
        }
    }
    public void importUnitsExcel(MultipartFile file) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() ==0) continue;

                DataFormatter formatter = new DataFormatter();
                Cell cell = row.getCell(0);
                String code;
                if (cell != null) {

                    code = formatter.formatCellValue(cell).trim();
                } else {

                    code = "";
                }

                String systemUrl = row.getCell(1).getStringCellValue().trim();
                String display = row.getCell(2).getStringCellValue().trim();
                System.out.println(systemUrl);
                System.out.println(code);
                System.out.println(display);
                CodeSystem system = codeSystemRepo.findBySystemName(systemUrl)
                        .orElseGet(() -> {
                            CodeSystem newSystem = new CodeSystem();
                            newSystem.setSystemName(systemUrl);
                            return codeSystemRepo.save(newSystem);
                        });

                Concepts concept = new Concepts();
                concept.setConceptName(display);
                concept.setDescription(display);
                concept.setType("Units");
                concept = conceptRepo.save(concept);

                ConceptCodeMapper mapping = new ConceptCodeMapper();
                mapping.setCode(code);
                mapping.setSystemId(system.getSystemId());
                mapping.setConceptId(concept.getConceptId());
                mapping.setDisplayText(concept.getConceptName());

                conceptCodeMapperRepo.save(mapping);


            }
        }
    }
}