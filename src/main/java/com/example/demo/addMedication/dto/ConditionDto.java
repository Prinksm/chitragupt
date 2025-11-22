package com.example.demo.addMedication.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConditionDto {

        private String icd10Code;
        private String conceptName;
        private String description;


        // Getters & setters


}
