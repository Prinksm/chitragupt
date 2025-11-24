package com.example.demo.repository.medications;

import com.example.demo.entity.codeableConcept.CodeSystem;
import com.example.demo.entity.medications.Medications;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MedicationsRepo extends JpaRepository<Medications,Long> {


    Optional<Medications> findByBrandName(String brandName);
}
