package com.example.demo.addMedication.dto;

import lombok.*;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DosageResponseDto {
    private BigDecimal amount;
    private String amountUnitId;//get concept id
    private String routeId;//get concept id
    private String instruction;

}
