package com.example.demo.entity.medications;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medications", schema = "medication")
public class Medications{
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medications_generator")
    @SequenceGenerator(
            name = "medications_generator",
            sequenceName = "medications_id_seq",
            schema = "medication",
            allocationSize = 1
    )
    @Column(name = "medication_id")
    private Long medicationId;
    @Column(name = "brand_name", nullable = false)
    private String brandName;
    @Column(name = "manufacturer_name", nullable = false)
    private String manufacturerName;
    @Column(name = "pack_size_label", length = 255)
    private String packSizeLabel;
    @Column(name = "type", nullable = false)
    private String type;
    @Column(name = "concept_id", nullable = false)
    private Long conceptId;
    @Column(name = "generic_name", length = 255)
    private String genericName;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "fhir_json", columnDefinition = "JSONB")
    private String fhirJson;
}