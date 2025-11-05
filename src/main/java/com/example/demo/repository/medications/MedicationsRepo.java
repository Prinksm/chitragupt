package com.example.demo.repository.medications;

import com.example.demo.entity.medications.Medications;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicationsRepo extends JpaRepository<Medications,Long> {
}
