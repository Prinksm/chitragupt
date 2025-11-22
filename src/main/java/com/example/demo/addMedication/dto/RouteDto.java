package com.example.demo.addMedication.dto;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RouteDto {
    private Long conceptId;
    private String conceptName;
}
