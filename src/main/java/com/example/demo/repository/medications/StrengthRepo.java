package com.example.demo.repository.medications;

import com.example.demo.entity.codeableConcept.Concepts;
import com.example.demo.entity.medications.Strengths;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;

public interface StrengthRepo extends JpaRepository<Strengths,Long> {
    Optional<Strengths> findByValueAndUnitId(BigDecimal value, Long conceptId);
}
