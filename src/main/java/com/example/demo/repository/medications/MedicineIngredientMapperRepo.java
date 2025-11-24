package com.example.demo.repository.medications;

import com.example.demo.entity.compositeKey.MedicineIngredientId;
import com.example.demo.entity.medications.MedicationIngredientMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicineIngredientMapperRepo extends JpaRepository<MedicationIngredientMapper, MedicineIngredientId> {
    boolean existsById(MedicineIngredientId id);
    List<MedicationIngredientMapper> findByMedicationId(Long id);
}
