CREATE SEQUENCE IF NOT EXISTS prescription_share_token_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS medication.prescription_share_token (
    id BIGINT PRIMARY KEY DEFAULT nextval('prescription_share_token_id_seq'),
    token VARCHAR(255) NOT NULL UNIQUE,
    patient_id BIGINT NOT NULL,
    prescription_ids JSONB NOT NULL,
    contact_id BIGINT,
    contact_value VARCHAR(255),
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL
);