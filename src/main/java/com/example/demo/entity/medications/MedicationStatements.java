package com.example.demo.entity.medications;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "medication_statements", schema = "medication")
public class MedicationStatements{

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "medication_statements_generator")
    @SequenceGenerator(
            name = "medication_statements_generator",
            sequenceName = "medication_statements_id_seq",
            schema = "public",
            allocationSize = 1
    )
    @Column(name = "statement_id")
    private Long statementId;

    @Column(name = "medication_id", nullable = false)
    private Long medicationId;

    @Column(name = "prescription_id", nullable = false)
    private Long prescriptionId;

    @Column(name = "dosage_id")
    private Long dosageId;

    @Column(name = "status", length = 50, nullable = false)
    private String status;


    @Column(name = "effective_start_date")
    private LocalDateTime effectiveStartDate;

    @Column(name = "effective_end_date")
    private LocalDateTime effectiveEndDate;

    @Column(name = "notes", length = 500)
    private String notes;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "fhir_json", columnDefinition = "JSONB")
    private String fhirJson;
}