package com.example.demo.addMedication.repo;

import com.example.demo.entity.medications.Dosages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DosageRepo extends JpaRepository<Dosages,Long> {
}
