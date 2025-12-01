package com.example.demo.addMedication.dto;

import lombok.*;

import java.math.BigDecimal;
import java.sql.Time;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TimingResponseDto {
    private Long timingId;
    private Integer frequency;
    private BigDecimal period;
    private String periodUnit;// period unit that timing: Once per 1 day
    private Time timeOfDay;
    private String whenCode; //TO be mapped from the frontend
}
