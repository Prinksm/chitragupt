package com.example.demo.addMedication.services;

import com.example.demo.addMedication.dto.AmountCodeDto;
import com.example.demo.addMedication.dto.MedicineDto;
import com.example.demo.addMedication.dto.RouteDto;
import com.example.demo.repository.codeableConcept.ConceptRepo;
import com.example.demo.repository.medications.MedicationsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AutoCompleteService {
    @Autowired
    MedicationsRepo medRepo;
    @Autowired
    ConceptRepo conceptRepo;

    public List<MedicineDto> searchMedicines(String keyword) {

        System.out.println("Service called");
        return medRepo.searchByBrandName(keyword);
    }
    public List<RouteDto> searchRoutes(String keyword) {

        System.out.println("Service called Routes");
        return conceptRepo.searchByRouteName(keyword);
    }
    public List<AmountCodeDto> searchAmountCode(String keyword) {

        System.out.println("Service called Routes");
        return conceptRepo.searchByAmountCodeName(keyword);
    }
}
