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
public class MedicineDoseId implements Serializable {
    @Column(name = "medication_id" ,nullable = false)
    private Long medicineId;
    @Column(name = "dose_form_id" ,nullable = false)
    private Long doseformId;
    @Override
    public boolean equals (Object o){
        if(this == o) return true;
        if(!(o instanceof MedicineDoseId )) return false;
        MedicineDoseId that = (MedicineDoseId) o;
        return Objects.equals(medicineId,that.medicineId)&&
                Objects.equals(doseformId,that.doseformId);

    }
    @Override
    public int hashCode(){
        return Objects.hash(medicineId, doseformId);
    }
}
