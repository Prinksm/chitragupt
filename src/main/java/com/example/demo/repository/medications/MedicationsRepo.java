package com.example.demo.repository.medications;

import com.example.demo.addMedication.dto.MedicineDto;
import com.example.demo.entity.medications.Medications;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface MedicationsRepo extends JpaRepository<Medications,Long> {


    Optional<Medications> findByBrandName(String brandName);
    List<Medications> findByFhirJsonIsNull();
    @Query("SELECT new com.example.demo.addMedication.dto.MedicineDto(m.id, m.brandName) " +
            "FROM Medications m " +
            "WHERE LOWER(m.brandName) LIKE LOWER(CONCAT( :brandName, '%'))")
    List<MedicineDto> searchByBrandName(String brandName);

}
