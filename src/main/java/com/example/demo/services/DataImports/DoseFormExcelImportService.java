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
                concept.setType("Dose Form");
                concept = conceptRepo.save(concept);

                ConceptCodeMapper mapping = new ConceptCodeMapper();
                mapping.setCode(code);
                mapping.setSystemId(system.getSystemId());
                mapping.setConceptId(concept.getConceptId());
                mapping.setDisplayText(concept.getConceptName());

                conceptCodeMapperRepo.save(mapping);

                DoseForms doseForm = new DoseForms();
                System.out.println("Concept:"+concept.getConceptId());
                doseForm.setConceptId(concept.getConceptId());
                doseFormRepo.save(doseForm);
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
                    // The formatCellValue() method returns the cell's value as a string.
                    code = formatter.formatCellValue(cell).trim();
                } else {
                    // Handle the case where the cell is null
                    code = "";
                }
//                String codes = row.getCell(1).getStringCellValue().trim();
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