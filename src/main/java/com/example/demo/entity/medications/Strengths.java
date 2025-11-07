package com.example.demo.entity.medications;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "strengths", schema = "medication")
public class Strengths {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "strengths_generator")
    @SequenceGenerator(
            name = "strengths_generator",
            sequenceName = "strengths_id_seq",
            schema = "public",
            allocationSize = 1
    )
    @Column(name = "strength_id")
    private Long strengthId;
    @Column(name = "value", precision = 10, scale = 4, nullable = false)
    private BigDecimal value;
    @Column(name = "unit_id ", nullable = false, unique = true)
    private Long  unitId;

}