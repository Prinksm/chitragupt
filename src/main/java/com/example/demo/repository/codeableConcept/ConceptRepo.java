package com.example.demo.repository.codeableConcept;

import com.example.demo.addMedication.dto.AmountCodeDto;
import com.example.demo.addMedication.dto.MedicineDto;
import com.example.demo.addMedication.dto.RouteDto;
import com.example.demo.entity.codeableConcept.Concepts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ConceptRepo extends JpaRepository<Concepts, Long> {
    Optional<Concepts> findByConceptNameAndType(String display, String type);

    @Query("SELECT new com.example.demo.addMedication.dto.RouteDto(c.conceptId, c.conceptName) " +
            "FROM Concepts c " +
            "WHERE c.type = 'Route' " +
            "AND LOWER(c.conceptName) LIKE LOWER(CONCAT(:name, '%'))")
    List<RouteDto> searchByRouteName(String name);

    @Query("SELECT new com.example.demo.addMedication.dto.AmountCodeDto(c.conceptId, c.conceptName) " +
            "FROM Concepts c " +
            "WHERE c.type = 'Amount-Unit' " +
            "AND LOWER(c.conceptName) LIKE LOWER(CONCAT(:name, '%'))")
    List<AmountCodeDto> searchByAmountCodeName(String keyword);
}
