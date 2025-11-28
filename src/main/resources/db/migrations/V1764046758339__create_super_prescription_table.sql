
CREATE SEQUENCE IF NOT EXISTS super_prescription_id_seq START WITH 1;

CREATE TABLE IF NOT EXISTS medication.super_prescription (
    super_prescription_id BIGINT PRIMARY KEY DEFAULT nextval('super_prescription_id_seq'),
    patient_id BIGINT NOT NULL,
    doctor_name VARCHAR(200) ,
    prescription_date DATE NOT NULL,
    notes VARCHAR(500),
    created_at TIMESTAMP DEFAULT now(),
    updated_at TIMESTAMP DEFAULT now(),
    FOREIGN KEY (patient_id) REFERENCES patient.patient(id)
);
ALTER TABLE medication.prescription
ADD COLUMN super_prescription_id BIGINT NOT NULL;

ALTER TABLE medication.prescription
ADD FOREIGN KEY (super_prescription_id)
REFERENCES medication.super_prescription(super_prescription_id);

ALTER TABLE medication.prescription
DROP CONSTRAINT IF EXISTS prescription_patient_id_fkey;

ALTER TABLE medication.prescription
DROP COLUMN IF EXISTS patient_id;
