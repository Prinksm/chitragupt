package com.example.demo.entity.medications;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "timing", schema = "medication")
public class Timing {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timing_generator")
    @SequenceGenerator(
            name = "timing_generator",
            sequenceName = "timing_id_seq",
            schema = "public",
            allocationSize = 1
    )
    @Column(name = "timing_id")
    private Long timingId;

    @Column(name = "frequency")
    private Integer frequency;

    @Column(name = "period", precision = 10, scale = 4)
    private BigDecimal period;

    @Column(name = "period_unit_id")
    private Long periodUnitId;


    @Column(name = "time_of_day")
    private Time timeOfDay;


    @Column(name = "when_code_id")
    private Long whenCodeId;


}