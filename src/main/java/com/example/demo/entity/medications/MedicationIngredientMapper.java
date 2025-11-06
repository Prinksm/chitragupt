package com.example.demo.entity.medications;

import com.example.demo.entity.compositeKey.MedicineIngredientId;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="medication_ingredients_mapping",schema="medication")
public class MedicationIngredientMapper {
    @EmbeddedId
    private MedicineIngredientId id;
    @Column(name = "medication_id" ,nullable = false,insertable = false, updatable = false)
    private Long  medicationId;
    @Column(name = "ingredient_id" ,nullable = false,insertable = false, updatable = false)
    private Long ingredientId;
    @Column(name = "strength_id" ,nullable = false)
    private Long strengthId;
}
