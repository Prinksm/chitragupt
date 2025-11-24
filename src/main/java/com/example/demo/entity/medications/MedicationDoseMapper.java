package com.example.demo.entity.medications;

import com.example.demo.entity.compositeKey.MedicineDoseId;
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
@Table(name="medication_dose_forms_mapping",schema="medication")
public class MedicationDoseMapper {
    @EmbeddedId
    private MedicineDoseId id;
    @Column(name = "medication_id" ,nullable = false,insertable = false, updatable = false)
    private Long  medicationId;
    @Column(name = "dose_form_id" ,nullable = false,insertable = false, updatable = false)
    private Long doseformId;

}
