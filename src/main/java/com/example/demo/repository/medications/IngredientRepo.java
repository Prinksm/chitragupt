package com.example.demo.repository.medications;

import com.example.demo.entity.medications.Ingredients;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepo extends JpaRepository<Ingredients,Long> {
}
