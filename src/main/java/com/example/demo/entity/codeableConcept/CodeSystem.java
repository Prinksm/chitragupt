package com.example.demo.entity.codeableConcept;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "code_system", schema = "codeable_concept")
public class CodeSystem  {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "code_system_generator")
    @SequenceGenerator(
            name = "code_system_generator",
            sequenceName = "code_system_id_seq",
            schema = "public",
            allocationSize = 1
    )
    @Column(name = "system_id")
    private Long systemId;

    @Column(name = "system_name", nullable = false, unique = true)
    private String systemName;

    @Column(name = "version")
    private String version;
}
