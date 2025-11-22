package com.example.demo.addMedication.dto;

import lombok.*;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DosageRequestDto {
    private BigDecimal amount;
    private Long amountUnitId;//get concept id
    private Long routeId;//get concept id
    private String instruction;

}
