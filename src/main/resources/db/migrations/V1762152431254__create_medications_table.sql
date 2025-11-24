CREATE SCHEMA IF NOT EXISTS medication;
CREATE SEQUENCE IF NOT EXISTS ingredients_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS medication.ingredients (
    ingredient_id BIGINT PRIMARY KEY DEFAULT nextval('ingredients_id_seq'),
    concept_id BIGINT NOT NULL,
    FOREIGN KEY (concept_id) REFERENCES codeable_concept.concepts(concept_id)
);

CREATE SEQUENCE IF NOT EXISTS dose_forms_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS medication.dose_forms (
    dose_form_id BIGINT PRIMARY KEY DEFAULT nextval('dose_forms_id_seq'),
    concept_id BIGINT NOT NULL,
    FOREIGN KEY (concept_id) REFERENCES codeable_concept.concepts(concept_id)
);

CREATE SEQUENCE IF NOT EXISTS strengths_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS medication.strengths (
    strength_id BIGINT PRIMARY KEY DEFAULT nextval('strengths_id_seq'),
    value DECIMAL(10, 4) NOT NULL,
    unit_id BIGINT NOT NULL,
    FOREIGN KEY (unit_id) REFERENCES codeable_concept.concepts(concept_id)
);

CREATE SEQUENCE IF NOT EXISTS medications_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS medication.medications (
    medication_id BIGINT PRIMARY KEY DEFAULT nextval('medications_id_seq'),
    brand_name VARCHAR NOT NULL,
    manufacturer_name VARCHAR NOT NULL,
    pack_size_label VARCHAR(255),
    type VARCHAR NOT NULL,
    concept_id BIGINT NOT NULL,
    generic_name VARCHAR(255),
    fhir_json JSONB,
    FOREIGN KEY (concept_id) REFERENCES codeable_concept.concepts(concept_id)
);

CREATE TABLE IF NOT EXISTS medication_ingredients_mapping (
    medication_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    strength_id BIGINT NOT NULL,
    PRIMARY KEY (medication_id, ingredient_id),
    FOREIGN KEY (medication_id) REFERENCES medication.medications(medication_id),
    FOREIGN KEY (ingredient_id) REFERENCES medication.ingredients(ingredient_id),
    FOREIGN KEY (strength_id) REFERENCES medication.strengths(strength_id)
);

CREATE TABLE IF NOT EXISTS medication_dose_forms_mapping (
    medication_id BIGINT NOT NULL,
    dose_form_id BIGINT NOT NULL,
    PRIMARY KEY (medication_id, dose_form_id),
    FOREIGN KEY (medication_id) REFERENCES medication.medications(medication_id),
    FOREIGN KEY (dose_form_id) REFERENCES medication.dose_forms(dose_form_id)
);



CREATE SEQUENCE IF NOT EXISTS prescription_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS medication.prescription(
    prescription_id BIGINT PRIMARY KEY DEFAULT nextval('prescription_id_seq'),
    patient_id BIGINT NOT NULL,
    reason_id BIGINT NOT NULL,
    notes VARCHAR(500),
    FOREIGN KEY (patient_id) REFERENCES patient.patient(id),
    FOREIGN KEY (reason_id) REFERENCES codeable_concept.concepts(concept_id)
);


-- Medication Statement Tables
CREATE SEQUENCE IF NOT EXISTS timing_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS medication.timing (
    timing_id BIGINT PRIMARY KEY DEFAULT nextval('timing_id_seq'),
    frequency INT,
    period DECIMAL(10, 4),
    period_unit_id BIGINT,
    time_of_day TIME,
    when_code_id BIGINT,
    FOREIGN KEY (period_unit_id) REFERENCES codeable_concept.concepts(concept_id),
    FOREIGN KEY (when_code_id) REFERENCES codeable_concept.concepts(concept_id)
);


CREATE SEQUENCE IF NOT EXISTS dosages_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS medication.dosages (
    dosage_id BIGINT PRIMARY KEY DEFAULT nextval('dosages_id_seq'),
    amount DECIMAL(10, 4),
    amount_unit_id BIGINT,
    route_id BIGINT,
    instruction TEXT,
    timing_id BIGINT,
    FOREIGN KEY (amount_unit_id) REFERENCES codeable_concept.concepts(concept_id),
    FOREIGN KEY (route_id) REFERENCES codeable_concept.concepts(concept_id),
    FOREIGN KEY (timing_id) REFERENCES medication.timing(timing_id)
);

CREATE SEQUENCE IF NOT EXISTS medication_statements_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS medication.medication_statements (
    statement_id BIGINT PRIMARY KEY DEFAULT nextval('medication_statements_id_seq'),
    medication_id BIGINT NOT NULL,
    prescription_id BIGINT NOT NULL,
    dosage_id BIGINT,
    status VARCHAR(50) NOT NULL,
    effective_start_date TIMESTAMP,
    effective_end_date TIMESTAMP,
    notes VARCHAR(500),
    fhir_json JSONB,
    FOREIGN KEY (medication_id) REFERENCES medication.medications(medication_id),
    FOREIGN KEY (dosage_id) REFERENCES medication.dosages(dosage_id),
    FOREIGN KEY (prescription_id) REFERENCES medication.prescription(prescription_id)
);
