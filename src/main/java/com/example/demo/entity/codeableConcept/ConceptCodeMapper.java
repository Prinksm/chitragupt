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
@Table(name = "concept_codings_mapping", schema = "codeable_concept")
public class ConceptCodeMapper {
    @Id
    @Column(name = "code", nullable = false, length = 255)
    private String code;

    @Column(name = "concept_id", nullable = false)
    private Long conceptId;

    @Column(name = "system_id", nullable = false)
    private Long systemId;

    @Column(name = "display_text", length = 255)
    private String displayText;
}
