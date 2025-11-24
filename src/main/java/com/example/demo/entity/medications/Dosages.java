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
@Table(name = "dosages", schema = "medication")
public class Dosages {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dosages_generator")
    @SequenceGenerator(
            name = "dosages_generator",
            sequenceName = "dosages_id_seq",
            schema = "public",
            allocationSize = 1
    )
    @Column(name = "dosage_id")
    private Long dosageId;

    @Column(name = "amount", precision = 10, scale = 4)
    private BigDecimal amount;

    @Column(name = "amount_unit_id")
    private Long amountUnitId;

    @Column(name = "route_id")
    private Long routeId;

    @Lob
    @Column(name = "instruction")
    private String instruction;

    @Column(name = "timing_id")
    private Long timingId;



}
