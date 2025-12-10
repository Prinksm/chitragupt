CREATE SEQUENCE IF NOT EXISTS medication_logs_id_seq START WITH 1;

CREATE TABLE IF NOT EXISTS medication.patient_medication_logs (
    medication_logs_id BIGINT PRIMARY KEY DEFAULT nextval('medication_logs_id_seq'),
    patient_id BIGINT NOT NULL,
    super_prescription_id BIGINT NOT NULL,
    prescription_id BIGINT NOT NULL,
    statement_id BIGINT NOT NULL,
    taken BOOLEAN, -- TRUE if completed, FALSE if skipped
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now(),
    FOREIGN KEY (patient_id) REFERENCES patient.patient(id),
    FOREIGN KEY (super_prescription_id) REFERENCES medication.super_prescription(super_prescription_id),
    FOREIGN KEY (prescription_id) REFERENCES medication.prescription(prescription_id),
    FOREIGN KEY (statement_id) REFERENCES medication.medication_statements(statement_id)
);
