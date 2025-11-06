package com.example.demo.repository.medications;

import com.example.demo.entity.medications.Ingredients;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IngredientRepo extends JpaRepository<Ingredients,Long> {
    Optional<Ingredients> findByConceptId(Long id);
}
