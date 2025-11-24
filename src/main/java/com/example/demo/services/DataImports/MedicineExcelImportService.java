package com.example.demo.services.DataImports;

import com.example.demo.entity.codeableConcept.CodeSystem;
import com.example.demo.entity.codeableConcept.ConceptCodeMapper;
import com.example.demo.entity.codeableConcept.Concepts;
import com.example.demo.entity.compositeKey.MedicineIngredientId;
import com.example.demo.entity.medications.*;
import com.example.demo.repository.codeableConcept.CodeSystemRepo;
import com.example.demo.repository.codeableConcept.ConceptCodeMapperRepo;
import com.example.demo.repository.codeableConcept.ConceptRepo;
import com.example.demo.repository.medications.*;
import org.apache.poi.ss.usermodel.*;
import org.hl7.fhir.r4.model.Composition;
import org.hl7.fhir.r4.model.Medication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private static  final  int MAX_RXNAV_CALLS =15;
    private static long lastResetTime = System.currentTimeMillis();
    private static AtomicInteger callCount = new AtomicInteger(0);
    private final RestTemplate restTemplate = new RestTemplate();

    public void importMedicineExcel(MultipartFile file) throws IOException {
        try (Workbook workbook = WorkbookFactory.create(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) continue;
                String brandName = row.getCell(1).getStringCellValue().trim();
                String manufacturerName = row.getCell(4).getStringCellValue().trim();
                String packSizeLabel =row.getCell(6).getStringCellValue().trim();
                String type =row.getCell(5).getStringCellValue().trim();
                String shortComposition1 =row.getCell(7).getStringCellValue().trim();
                String shortComposition2 = null;
                Cell cell = row.getCell(8);
                if (cell != null) {
                    // It's also good practice to ensure it's the correct cell type before calling getStringCellValue()
                    if (cell.getCellType() == CellType.STRING) {
                        shortComposition2 = cell.getStringCellValue().trim();
                    } else {
                        shortComposition2 = cell.toString().trim();
                    }
                }
                System.out.println(brandName);
                System.out.println(manufacturerName);
                System.out.println(packSizeLabel);
                System.out.println(type);
                System.out.println(shortComposition1);
                System.out.println(shortComposition2);

                //Medication Table data storage
                Medications medicine = medicationsRepo.findByBrandName(brandName)
                        .orElseGet(() -> {
                            Medications m = new Medications();
                            m.setBrandName(brandName);
                            m.setManufacturerName(manufacturerName);
                            m.setType(type);
                            String genericName = extractIngredientName(shortComposition1);
                            m.setGenericName(genericName);
                            m.setPackSizeLabel(packSizeLabel);
                            //Check for the concept id
                            Concepts concept = conceptRepo.findByConceptNameAndType(genericName,"Ingredients")
                                    .orElseGet(()->{
                                        String systemUrl = "http://www.nlm.nih.gov/research/umls/rxnorm";
                                        String code = fetchRxCui(genericName);

                                        CodeSystem system = codeSystemRepo.findBySystemName(systemUrl)
                                                .orElseGet(() -> {
                                                    CodeSystem newSystem = new CodeSystem();
                                                    newSystem.setSystemName(systemUrl);
                                                    return codeSystemRepo.save(newSystem);
                                                });
                                        Concepts c = new Concepts();
                                        c.setConceptName(genericName);
                                        c.setDescription(genericName);
                                        c.setType("Ingredients");
                                        c = conceptRepo.save(c);

                                        ConceptCodeMapper mapping = new ConceptCodeMapper();
                                        mapping.setCode(code);
                                        mapping.setSystemId(system.getSystemId());
                                        mapping.setConceptId(c.getConceptId());
                                        mapping.setDisplayText(c.getConceptName());

                                        conceptCodeMapperRepo.save(mapping);
                                        return c;
                                    });
                            m.setConceptId(concept.getConceptId());

                            System.out.println(m);
                            return medicationsRepo.save(m);
                        });

                // check for the ingredients of the medicine
                List<String> ingredients = Arrays.asList(shortComposition1,shortComposition2);
                for(String ingStr : ingredients){
                    //Check the ingredient
                    //map the strength and the ingredients

                    if(ingStr != null)
                    {
                        //Maping ingredient with the conceptid
                        String ingredientName = extractIngredientName(ingStr);

                        Concepts concept = conceptRepo.findByConceptNameAndType(ingredientName,"Ingredients")
                                .orElseGet(()->{
                                    String systemUrl = "http://www.nlm.nih.gov/research/umls/rxnorm";
                                    String code = fetchRxCui(ingredientName);

                                    CodeSystem system = codeSystemRepo.findBySystemName(systemUrl)
                                            .orElseGet(() -> {
                                                CodeSystem newSystem = new CodeSystem();
                                                newSystem.setSystemName(systemUrl);
                                                return codeSystemRepo.save(newSystem);
                                            });
                                    Concepts c = new Concepts();
                                    c.setConceptName(ingredientName);
                                    c.setDescription(ingredientName);
                                    c.setType("Ingredients");
                                    c = conceptRepo.save(c);

                                    ConceptCodeMapper mapping = new ConceptCodeMapper();
                                    mapping.setCode(code);
                                    mapping.setSystemId(system.getSystemId());
                                    mapping.setConceptId(c.getConceptId());
                                    mapping.setDisplayText(c.getConceptName());

                                    conceptCodeMapperRepo.save(mapping);
                                    return c;
                                });
                        Long Id = concept.getConceptId();
                        //Ingredients mapping to concept ids
                        Ingredients ing = ingredientRepo.findByConceptId(Id)
                                .orElseGet(()->{
                                    Ingredients i = new Ingredients();
                                    i.setConceptId(Id);
                                    return ingredientRepo.save(i);
                                });

                        //Medicine-Ingredient-Strength mapping
                        String ingStrength = parseStrengthText(ingStr);
                        System.out.println(ingStr +"  "+ingStrength);
                        String[] strength = splitStrength(ingStrength);
                        System.out.println(strength[0]);
                        System.out.println(strength[1]);

                        BigDecimal value = new BigDecimal(strength[0]);
                        String unit = strength[1].toLowerCase();
                        Concepts unitConcept = conceptRepo.findAll().stream()
                                .filter(c -> c.getType().equalsIgnoreCase("UNITS"))
                                .filter(c-> c.getConceptName() != null && c.getConceptName().equalsIgnoreCase(unit))
                                .findFirst()
                                .orElseThrow(() ->new NoSuchElementException("unit not exist"));

                        Strengths strengths = strengthRepo.findByValueAndUnitId(value,unitConcept.getConceptId())
                                .orElseGet(()->{
                                    Strengths s = new Strengths();
                                    s.setValue(value);
                                    s.setUnitId(unitConcept.getConceptId());

                                    return strengthRepo.save(s);

                                });

                        MedicineIngredientId id = new MedicineIngredientId(medicine.getMedicationId(),ing.getIngredientId());

                        boolean exists  = medicineIngredientMapperRepo.existsById(id);
                      if(!exists){
                            MedicationIngredientMapper medicationIngredientMap =  new MedicationIngredientMapper();
                            medicationIngredientMap.setId(id);
                            medicationIngredientMap.setMedicationId(medicine.getMedicationId());
                            medicationIngredientMap.setIngredientId(ing.getIngredientId());
                            medicationIngredientMap.setStrengthId(strengths.getStrengthId());
                            medicineIngredientMapperRepo.save(medicationIngredientMap);
                        }



                    }


                }

            }
        }
    }

    private String extractIngredientName(String Composition) {
        if(Composition == null || Composition.isBlank()) return Composition;
        return Composition.replaceAll("\\(.*?\\)","").trim();
    }

    //Checking the api hit limits
    private  synchronized void rateLimitRxNav() {
        long now = System.currentTimeMillis();
        if(now - lastResetTime >1000){
            callCount.set(0);
            lastResetTime = now;
        }

        if(callCount.incrementAndGet()>MAX_RXNAV_CALLS){
            long sleepTime = 1000-(now - lastResetTime);
            if(sleepTime > 0){
                try{
                    Thread.sleep(sleepTime);

                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            callCount.set(0);
            lastResetTime = System.currentTimeMillis();
        }
    }

    private String fetchRxCui(String composition) {
        rateLimitRxNav(); // limit to 20/sec
        try {


            String url = "https://rxnav.nlm.nih.gov/REST/rxcui.json?name=" + composition;
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                Map<String, Object> idGroup = (Map<String, Object>) response.getBody().get("idGroup");
                if (idGroup != null && idGroup.get("rxnormId") != null) {
                    java.util.List<String> ids = (java.util.List<String>) idGroup.get("rxnormId");
                    if (!ids.isEmpty()) {
                        return ids.get(0);
                    }
                }
            }
            return "UNKNOWN"; // not found
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        }
    }
    private String parseStrengthText(String composition) {
        if (composition == null || composition.isBlank()) return "";
        // Extract content inside parentheses
        Pattern pattern = Pattern.compile("\\(([^)]+)\\)");
        Matcher matcher = pattern.matcher(composition);
        if (matcher.find()) {

            String inside = matcher.group(1).replaceAll("\\s+", "").toLowerCase();

            if (inside.contains("/")) {
                Pattern ratioPattern = Pattern.compile("([0-9.]+)(mg|mcg|g|iu)\\/([0-9.]+)(ml|l)", Pattern.CASE_INSENSITIVE);
                Matcher m = ratioPattern.matcher(inside);
                if (m.find()) {
                    double numValue = Double.parseDouble(m.group(1));
                    String numUnit = m.group(2).toLowerCase();
                    double denomValue = Double.parseDouble(m.group(3));
                    String denomUnit = m.group(4).toLowerCase();
                    // Convert mcg → mg
                    if (numUnit.equals("mcg")) {
                        numValue = numValue / 1000.0;
                        numUnit = "mg";
                    }
                    // Normalize IU → [IU]
                    if (numUnit.equals("iu")) {
                        numUnit = "[IU]";
                    }
                    double ratio = numValue / denomValue;
                    return ratio + numUnit + "/" + denomUnit;
                }
            }

            if (inside.contains("%")) {
                Pattern percentPattern = Pattern.compile("([0-9.]+)%");
                Matcher m = percentPattern.matcher(inside);
                if (m.find()) {
                    return m.group(1) + "%"; // e.g. "2%"
                }
            }

            Pattern simplePattern = Pattern.compile("([0-9.]+)(mg|mcg|g|ml|iu)", Pattern.CASE_INSENSITIVE);
            Matcher m = simplePattern.matcher(inside);
            if (m.find()) {
                double value = Double.parseDouble(m.group(1));
                String unit = m.group(2).toLowerCase();

                if (unit.equals("mcg")) {
                    value = value / 1000.0;
                    unit = "mg";
                }
                if (unit.equals("iu")) {
                    unit = "[IU]";
                }

                if (value == (int) value) {
                    return ((int) value) + unit;
                } else {
                    return value + unit;
                }
            }
        }
        return ""; // fallback
    }

    private String[] splitStrength(String strengthStr) {
        String[] result = new String[2];
        result[0] = "0.0";       // default value
        result[1] = "UNKNOWN";   // default unit
        if (strengthStr == null || strengthStr.isBlank()) {
            return result;
        }

        strengthStr = strengthStr.replaceAll("\\s+", "").toLowerCase();

        if (strengthStr.contains("/")) {
            Pattern ratioPattern = Pattern.compile("([0-9.]+)([a-zA-Z\\[\\]]+\\/[a-zA-Z\\[\\]]+)", Pattern.CASE_INSENSITIVE);
            Matcher m = ratioPattern.matcher(strengthStr);
            if (m.find()) {
                String value = m.group(1);
                String unit = m.group(2).toLowerCase();
                // Normalize IU → [IU]
                if (unit.contains("iu")) {
                    unit = unit.replaceAll("iu", "[IU]");
                }
                result[0] = value;
                result[1] = unit;
                return result;
            }
        }

        Pattern simplePattern = Pattern.compile("([0-9.]+)([a-zA-Z%\\[\\]]+)", Pattern.CASE_INSENSITIVE);
        Matcher m = simplePattern.matcher(strengthStr);
        if (m.find()) {
            String value = m.group(1);
            String unit = m.group(2).toLowerCase();

            if (unit.contains("iu")) {
                unit = "[IU]";
            }
            result[0] = value;
            result[1] = unit;
            return result;
        }
        return result;
    }

}
