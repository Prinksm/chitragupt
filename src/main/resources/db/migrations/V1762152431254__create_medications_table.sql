
CREATE SEQUENCE IF NOT EXISTS medication.ingredients_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS medication.ingredients (
    ingredient_id BIGINT PRIMARY KEY DEFAULT nextval('medication.ingredients_id_seq'),
    concept_id INT NOT NULL,
    FOREIGN KEY (concept_id) REFERENCES codeable_concept.concepts(concept_id)
);

CREATE SEQUENCE IF NOT EXISTS medication.dose_forms_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS medication.dose_forms (
    dose_form_id BIGINT PRIMARY KEY DEFAULT nextval('medication.dose_forms_id_seq'),
    concept_id INT NOT NULL,
    FOREIGN KEY (concept_id) REFERENCES codeable_concept.concepts(concept_id)
);

CREATE SEQUENCE IF NOT EXISTS medication.strengths_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS medication.strengths (
    strength_id BIGINT PRIMARY KEY DEFAULT nextval('medication.strengths_id_seq'),
    value DECIMAL(10, 4) NOT NULL,
    unit_id INT NOT NULL,
    FOREIGN KEY (unit_id) REFERENCES codeable_concept.concepts(concept_id)
);

CREATE SEQUENCE IF NOT EXISTS medication.medications_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS medication.medications (
    medication_id BIGINT PRIMARY KEY DEFAULT nextval('medication.medications_id_seq'),
    brand_name VARCHAR NOT NULL,
    manufacturer_name VARCHAR NOT NULL,
    pack_size_label VARCHAR(255),
    type VARCHAR NOT NULL,
    concept_id INT NOT NULL,
    generic_name VARCHAR(255),
    fhir_json JSONB,
    FOREIGN KEY (concept_id) REFERENCES codeable_concept.concepts(concept_id)
);

CREATE TABLE IF NOT EXISTS medication.medication_ingredients_mapping (
    medication_id BIGINT NOT NULL,
    ingredient_id BIGINT NOT NULL,
    strength_id BIGINT NOT NULL,
    PRIMARY KEY (medication_id, ingredient_id),
    FOREIGN KEY (medication_id) REFERENCES medication.medications(medication_id),
    FOREIGN KEY (ingredient_id) REFERENCES medication.ingredients(ingredient_id),
    FOREIGN KEY (strength_id) REFERENCES medication.strengths(strength_id)
);

CREATE TABLE IF NOT EXISTS medication.medication_dose_forms_mapping (
    medication_id BIGINT NOT NULL,
    dose_form_id BIGINT NOT NULL,
    PRIMARY KEY (medication_id, dose_form_id),
    FOREIGN KEY (medication_id) REFERENCES medication.medications(medication_id),
    FOREIGN KEY (dose_form_id) REFERENCES medication.dose_forms(dose_form_id)
);


-- Prescription Table (must come before Medication Statements as it's a foreign key dependency)
CREATE SEQUENCE IF NOT EXISTS medication.prescription_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS medication.prescription ( -- Renamed from `medication.prescription` to avoid potential reserved keyword issues
    prescription_id BIGINT PRIMARY KEY DEFAULT nextval('medication.prescription_id_seq'),
    patient_id INT NOT NULL,
    reason_id INT NOT NULL,
    notes VARCHAR(500),
    FOREIGN KEY (patient_id) REFERENCES patient.patient(id),
    FOREIGN KEY (reason_id) REFERENCES codeable_concept.concepts(concept_id)
);


-- Medication Statement Tables
CREATE SEQUENCE IF NOT EXISTS medication.timing_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS medication.timing (
    timing_id BIGINT PRIMARY KEY DEFAULT nextval('medication.timing_id_seq'),
    frequency INT,
    period DECIMAL(10, 4),
    period_unit_id INT,
    time_of_day TIME,
    when_code_id INT,
    FOREIGN KEY (period_unit_id) REFERENCES codeable_concept.concepts(concept_id),
    FOREIGN KEY (when_code_id) REFERENCES codeable_concept.concepts(concept_id)
);


CREATE SEQUENCE IF NOT EXISTS medication.dosages_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS medication.dosages (
    dosage_id BIGINT PRIMARY KEY DEFAULT nextval('medication.dosages_id_seq'),
    amount DECIMAL(10, 4),
    amount_unit_id INT,
    route_id INT,
    instruction TEXT,
    timing_id BIGINT, -- Changed to BIGINT to match parent table type
    FOREIGN KEY (amount_unit_id) REFERENCES codeable_concept.concepts(concept_id),
    FOREIGN KEY (route_id) REFERENCES codeable_concept.concepts(concept_id),
    FOREIGN KEY (timing_id) REFERENCES medication.timing(timing_id)
);

CREATE SEQUENCE IF NOT EXISTS medication.medication_statements_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS medication.medication_statements (
    statement_id BIGINT PRIMARY KEY DEFAULT nextval('medication.medication_statements_id_seq'),
    medication_id BIGINT NOT NULL,
    prescription_id BIGINT NOT NULL,  -- Changed to BIGINT to match parent table type
    dosage_id BIGINT,                 -- Changed to BIGINT to match parent table type
    status VARCHAR(50) NOT NULL,
    effective_start_date TIMESTAMP,   -- Changed DATETIME to TIMESTAMP (standard SQL type)
    effective_end_date TIMESTAMP,     -- Changed DATETIME to TIMESTAMP (standard SQL type)
    notes VARCHAR(500),
    fhir_json JSONB,
    FOREIGN KEY (medication_id) REFERENCES medication.medications(medication_id),
    FOREIGN KEY (dosage_id) REFERENCES medication.dosages(dosage_id),
    FOREIGN KEY (prescription_id) REFERENCES medication.prescription(prescription_id) -- Added FK constraint
);
