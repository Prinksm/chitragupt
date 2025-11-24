package com.example.demo.addMedication.controllers;

import com.example.demo.addMedication.dto.AmountCodeDto;
import com.example.demo.addMedication.dto.MedicineDto;
import com.example.demo.addMedication.dto.RouteDto;
import com.example.demo.addMedication.services.AutoCompleteService;
import org.hl7.fhir.r4.model.Medication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/patient/medication")
public class MedicineLookupController {
    //Routes get
    //medicines get
    @Autowired
    AutoCompleteService autoCompleteService;
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

}
