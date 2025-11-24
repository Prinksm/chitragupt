package com.example.demo.repository.medications;

import com.example.demo.entity.codeableConcept.CodeSystem;
import com.example.demo.entity.medications.DoseForms;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DoseFormRepo extends JpaRepository<DoseForms,Long> {

}

