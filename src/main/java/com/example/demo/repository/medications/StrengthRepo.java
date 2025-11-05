package com.example.demo.repository.medications;

import com.example.demo.entity.medications.Strengths;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StrengthRepo extends JpaRepository<Strengths,Long> {
}
