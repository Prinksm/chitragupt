package com.example.demo.entity.codeableConcept;

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
@Table(name = "concepts", schema = "codeable_concept")
public class Concepts {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "concepts_generator")
    @SequenceGenerator(
            name = "concepts_generator",
            sequenceName ="concept_id_seq",
            schema = "public",
            allocationSize = 1
    )
    @Column(name = "concept_id")
    private Long conceptId;

    @Column(name = "concept_name", nullable = false)
    private String conceptName;

    @Column(name = "description")
    private String description;
    @Column(name = "type",nullable = false)
    private String type;
}
