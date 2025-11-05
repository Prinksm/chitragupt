package com.example.demo.repository.medications;

import com.example.demo.entity.compositeKey.MedicineIngredientId;
import com.example.demo.entity.medications.MedicationIngredientMapper;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MedicineIngredientMapperRepo extends JpaRepository<MedicationIngredientMapper, MedicineIngredientId> {
}
