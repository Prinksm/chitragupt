package com.example.demo.addMedication.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AmountCodeDto {
    private Long conceptId;
    private String conceptName;
}
