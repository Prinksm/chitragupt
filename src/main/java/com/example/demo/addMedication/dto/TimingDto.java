package com.example.demo.addMedication.dto;

import lombok.*;
import org.aspectj.apache.bcel.generic.Instruction;

import java.math.BigDecimal;
import java.sql.Time;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimingDto {
    private Integer frequency;
    private BigDecimal period;
    private String periodUnit;// period unit that timing: Once per 1 day
    private Time timeOfDay;
    private String whenCode; //TO be mapped from the frontend
}
//{
//        "frequency": 3,
//        "period": 1,
//        "periodUnitId": 1,
//        "timeOfDay": "06:00:00",
//        "whenCodeId": 4
//        }
//
//Interpretation:
//
//Take 3 times per day (every 8 hours)
//
//First dose at 6:00 AM, next doses at 2:00 PM and 10:00 PM
//
//Context: “after meal”
//
//Instruction: “Take every 8 hours starting at 6:00 AM after meals.”