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
@Table(name = "ingredients", schema = "medication")
public class Ingredients {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ingredients_generator")
    @SequenceGenerator(
            name = "ingredients_generator",
            sequenceName = "medication.ingredients_id_seq",
            schema = "medication",
            allocationSize = 1
    )
    @Column(name = "ingredient_id")
    private Long ingredientId;

    @Column(name = "concept_id ", nullable = false, unique = true)
    private Long  conceptId;

}