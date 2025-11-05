package com.example.demo.services.DataImports;

import com.example.demo.entity.codeableConcept.CodeSystem;
import com.example.demo.entity.codeableConcept.ConceptCodeMapper;
import com.example.demo.entity.codeableConcept.Concepts;
import com.example.demo.entity.medications.DoseForms;
import com.example.demo.repository.codeableConcept.CodeSystemRepo;
import com.example.demo.repository.codeableConcept.ConceptCodeMapperRepo;
import com.example.demo.repository.codeableConcept.ConceptRepo;
import com.example.demo.repository.medications.*;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DecimalFormat;

@Service
public class MedicineExcelImportService {
    @Autowired
    CodeSystemRepo codeSystemRepo;
    @Autowired
    ConceptRepo conceptRepo;
    @Autowired
    ConceptCodeMapperRepo conceptCodeMapperRepo;
    @Autowired
    DoseFormRepo doseFormRepo;
    @Autowired
    MedicationsRepo medicationsRepo;
    @Autowired
    IngredientRepo ingredientRepo;
    @Autowired
    MedicineDoseMapperRepo medicineDoseMapperRepo;
    @Autowired
    StrengthRepo strengthRepo;
    @Autowired
    MedicineIngredientMapperRepo medicineIngredientMapperRepo;


    public void importMedicineExcel(MultipartFile file) throws IOException {
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

            }
        }
    }
}
