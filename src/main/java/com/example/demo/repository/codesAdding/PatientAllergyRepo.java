package com.example.demo.repository.codesAdding;


import com.example.demo.entity.patientEntity.PatientAllergy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientAllergyRepo extends JpaRepository<PatientAllergy,Long> {
    Optional<PatientAllergy> findByAllergyType(String allergyType);
}
