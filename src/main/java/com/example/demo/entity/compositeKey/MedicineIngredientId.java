package com.example.demo.entity.compositeKey;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class MedicineIngredientId implements Serializable {
    @Column(name = "medication_id" ,nullable = false)
    private Long medicineId;
    @Column(name = "ingredient_id" ,nullable = false)
    private Long ingredientId;
    @Override
    public boolean equals (Object o){
        if(this == o) return true;
        if(!(o instanceof MedicineIngredientId)) return false;
        MedicineIngredientId that = (MedicineIngredientId) o;
        return Objects.equals(medicineId,that.medicineId)&&
                Objects.equals(ingredientId,that.ingredientId);

    }
    @Override
    public int hashCode(){
        return Objects.hash(medicineId,ingredientId);
    }
}
