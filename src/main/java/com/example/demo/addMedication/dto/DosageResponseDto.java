package com.example.demo.addMedication.dto;

import lombok.*;

import java.math.BigDecimal;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DosageResponseDto {
    private Long dosageId;
    private BigDecimal amount;
    private String amountUnit;//get concept id
    private Long amountUnitId;
    private String route;//get concept id
    private Long routeId;
    private String instruction;

}
