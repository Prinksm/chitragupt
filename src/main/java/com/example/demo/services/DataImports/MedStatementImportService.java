package com.example.demo.services.DataImports;

import com.example.demo.entity.codeableConcept.CodeSystem;
import com.example.demo.entity.codeableConcept.ConceptCodeMapper;
import com.example.demo.entity.codeableConcept.Concepts;
import com.example.demo.repository.codeableConcept.CodeSystemRepo;
import com.example.demo.repository.codeableConcept.ConceptCodeMapperRepo;
import com.example.demo.repository.codeableConcept.ConceptRepo;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
@Service
public class MedStatementImportService {

    @Autowired
    CodeSystemRepo codeSystemRepo;

    @Autowired
    ConceptRepo conceptRepo;
    @Autowired
    ConceptCodeMapperRepo conceptCodeMapperRepo;
    public void importWhenCodeExcel(MultipartFile file) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() ==0) continue;

                DataFormatter formatter = new DataFormatter();
                Cell cell = row.getCell(1);
                String code;
                if (cell != null) {

                    code = formatter.formatCellValue(cell).trim();
                } else {

                    code = "";
                }

                String systemUrl = row.getCell(0).getStringCellValue().trim();
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

                Concepts concept = conceptRepo.findByConceptNameAndType(display,"When-Timing")
                        .orElseGet(()->{
                            Concepts c =new Concepts();
                            c.setConceptName(display);
                            c.setDescription(display);
                            c.setType("When-Timing");
                           return conceptRepo.save(c);
                        });



                ConceptCodeMapper mapping = new ConceptCodeMapper();
                mapping.setCode(code);
                mapping.setSystemId(system.getSystemId());
                mapping.setConceptId(concept.getConceptId());
                mapping.setDisplayText(concept.getConceptName());

                conceptCodeMapperRepo.save(mapping);


            }
        }
    }
    public void importRouteCodeExcel(MultipartFile file) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() ==0) continue;

                DataFormatter formatter = new DataFormatter();
                Cell cell = row.getCell(1);
                String code;
                if (cell != null) {

                    code = formatter.formatCellValue(cell).trim();
                } else {

                    code = "";
                }

                String systemUrl = row.getCell(0).getStringCellValue().trim();
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

                Concepts concept = conceptRepo.findByConceptNameAndType(display,"Route")
                        .orElseGet(()->{
                            Concepts c =new Concepts();
                            c.setConceptName(display);
                            c.setDescription(display);
                            c.setType("Route");
                            return conceptRepo.save(c);
                        });



                ConceptCodeMapper mapping = new ConceptCodeMapper();
                mapping.setCode(code);
                mapping.setSystemId(system.getSystemId());
                mapping.setConceptId(concept.getConceptId());
                mapping.setDisplayText(concept.getConceptName());

                conceptCodeMapperRepo.save(mapping);


            }
        }
    }
    public void importAmountCodeExcel(MultipartFile file) throws IOException {
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

                String systemUrl = row.getCell(2).getStringCellValue().trim();
                String display = row.getCell(1).getStringCellValue().trim();

                System.out.println(systemUrl);
                System.out.println(code);
                System.out.println(display);
                CodeSystem system = codeSystemRepo.findBySystemName(systemUrl)
                        .orElseGet(() -> {
                            CodeSystem newSystem = new CodeSystem();
                            newSystem.setSystemName(systemUrl);
                            return codeSystemRepo.save(newSystem);
                        });

                Concepts concept = conceptRepo.findByConceptNameAndType(display,"Amount-Unit")
                        .orElseGet(()->{
                            Concepts c =new Concepts();
                            c.setConceptName(display);
                            c.setDescription(display);
                            c.setType("Amount-Unit");
                            return conceptRepo.save(c);
                        });



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
