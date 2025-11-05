package com.example.demo.repository.medications;

import com.example.demo.entity.compositeKey.MedicineDoseId;
import com.example.demo.entity.medications.MedicationDoseMapper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineDoseMapperRepo extends JpaRepository<MedicationDoseMapper, MedicineDoseId> {
}
