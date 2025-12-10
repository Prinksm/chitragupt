package com.example.demo.addMedication.controllers;

import com.example.demo.addMedication.dto.AmountCodeDto;
import com.example.demo.addMedication.dto.MedicineDto;
import com.example.demo.addMedication.dto.RouteDto;
import com.example.demo.addMedication.services.AutoCompleteService;
import com.example.demo.emails.dto.MedicationReminderRequest;
import com.example.demo.emails.services.MedicationReminderService;
import org.hl7.fhir.r4.model.Medication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/patient/medication")
public class MedicineLookupController {
    //Routes get
    //medicines get
    @Autowired
    AutoCompleteService autoCompleteService;
    @Autowired
    private MedicationReminderService reminderService;


    @PostMapping("/send-reminder")
    public ResponseEntity<Map<String, String>>  sendReminder(@RequestBody MedicationReminderRequest req) {

        reminderService.sendReminder(
                req.getSuperPrescriptionId(),
                req.getMedicationName(),
                req.getDoseTime(),
                req.getReminderType()
        );

        Map<String, String> response = Map.of("message", "Reminder Sent Successfully");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/medicine-search")
    public List<MedicineDto> medicineSearch(@RequestParam String q) {
        System.out.println("Request data"+ q);
        List<MedicineDto> medicines =  autoCompleteService.searchMedicines(q);
        for( MedicineDto medicine : medicines)
        {
            System.out.println(medicine);
        }
        return medicines;
//        return autoCompleteService.searchMedicines(q);
    }
    @GetMapping("/route-search")
    public List<RouteDto> routeSearch(@RequestParam String q) {
        System.out.println("Request data"+ q);
        List<RouteDto> routes =  autoCompleteService.searchRoutes(q);

        for(RouteDto medicine :routes)
        {
            System.out.println(medicine);
        }
        return routes;
//        return autoCompleteService.searchMedicines(q);
    }
    @GetMapping("/amount-code-search")
    public List<AmountCodeDto> amountCodeSearch(@RequestParam String q) {
        return autoCompleteService.searchAmountCode(q);

    }
    @GetMapping("/conditions")
    public ResponseEntity<List<String>> getConditions(@RequestParam String term) {
        String url = "https://clinicaltables.nlm.nih.gov/api/conditions/v3/search?terms=" + term + "&maxList=20";
        RestTemplate restTemplate = new RestTemplate();

        Object[] response = restTemplate.getForObject(url, Object[].class);

        // Print the raw response
        System.out.println("Raw response from ClinicalTables API: " + Arrays.toString(response));

        List<String> conditions = new ArrayList<>();
        if (response != null && response.length > 3 && response[3] instanceof List) {
            List<?> rawList = (List<?>) response[3];
            for (Object item : rawList) {
                if (item instanceof List) {
                    List<?> innerList = (List<?>) item;
                    if (!innerList.isEmpty()) {
                        conditions.add(innerList.get(0).toString());
                    }
                }
            }
        }

        System.out.println("Parsed conditions list: " + conditions);

        return ResponseEntity.ok(conditions);
    }






}
