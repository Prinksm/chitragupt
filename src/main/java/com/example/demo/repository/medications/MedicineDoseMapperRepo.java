package com.example.demo.repository.medications;

import com.example.demo.entity.compositeKey.MedicineDoseId;
import com.example.demo.entity.medications.MedicationDoseMapper;
import com.example.demo.entity.medications.MedicationIngredientMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicineDoseMapperRepo extends JpaRepository<MedicationDoseMapper, MedicineDoseId> {
   Optional<MedicationDoseMapper> findByMedicationId(Long id);
}
