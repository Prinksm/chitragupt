package com.example.demo.entity.medications;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "dose_forms", schema = "medication")
public class DoseForms {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dose_forms_generator")
    @SequenceGenerator(
            name = "dose_forms_generator",
            sequenceName = "dose_forms_id_seq",
            schema = "public",
            allocationSize = 1
    )
    @Column(name = "dose_form_id")
    private Long doseformId;

    @Column(name = "concept_id ", nullable = false, unique = true)
    private Long  conceptId;

}