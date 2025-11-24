CREATE SEQUENCE IF NOT EXISTS patient_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS patient.patient (
    id BIGINT PRIMARY KEY DEFAULT nextval('patient_id_seq'),
    firstName VARCHAR(30) NOT NULL,
    middleName VARCHAR(30),
    lastName VARCHAR(30),
    birth_date DATE,
    gender VARCHAR(20),
    maritalStatus VARCHAR(30),
    is_active BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fhir JSONB
);


CREATE SEQUENCE IF NOT EXISTS patient_contact_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS patient.patient_contact (
    id BIGINT PRIMARY KEY DEFAULT nextval('patient_contact_id_seq'),
    patient_id BIGINT NOT NULL,
    relationship_type VARCHAR(50),
    firstName VARCHAR(30) NOT NULL,
    middleName VARCHAR(30),
    lastName VARCHAR(30),
    period_start DATE,
    period_end DATE,
    FOREIGN KEY (patient_id) REFERENCES patient.patient(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE SEQUENCE IF NOT EXISTS patient_address_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS patient.patient_address (
    id BIGINT PRIMARY KEY DEFAULT nextval('patient_address_id_seq'),
    patient_id BIGINT NOT NULL,
    address_id BIGINT NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patient.patient(id) ON DELETE CASCADE,
    FOREIGN KEY (address_id) REFERENCES users.common_address(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE SEQUENCE IF NOT EXISTS patient_telecom_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS patient.patient_telecom (
    id BIGINT PRIMARY KEY DEFAULT nextval('patient_telecom_id_seq'),
    patient_id BIGINT NOT NULL,
    telecom_id BIGINT NOT NULL,
    FOREIGN KEY (patient_id) REFERENCES patient.patient(id) ON DELETE CASCADE,
    FOREIGN KEY (telecom_id) REFERENCES users.common_telecom(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE SEQUENCE IF NOT EXISTS contact_address_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS patient.contact_address (
    id BIGINT PRIMARY KEY DEFAULT nextval('contact_address_id_seq'),
    contact_id BIGINT NOT NULL,
    address_id BIGINT NOT NULL,
    FOREIGN KEY (contact_id) REFERENCES patient.patient_contact(id) ON DELETE CASCADE,
    FOREIGN KEY (address_id) REFERENCES users.common_address(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE SEQUENCE IF NOT EXISTS contact_telecom_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS patient.contact_telecom (
    id BIGINT PRIMARY KEY DEFAULT nextval('contact_telecom_id_seq'),
    contact_id BIGINT NOT NULL,
    telecom_id BIGINT NOT NULL,
    FOREIGN KEY (contact_id) REFERENCES patient.patient_contact(id) ON DELETE CASCADE,
    FOREIGN KEY (telecom_id) REFERENCES users.common_telecom(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE SEQUENCE IF NOT EXISTS patient_allergy_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS patient.patient_allergy (
    id BIGINT PRIMARY KEY DEFAULT nextval('patient_allergy_id_seq'),
    patient_id BIGINT NOT NULL REFERENCES patient.patient(id) ON DELETE CASCADE,
    clinical_status VARCHAR(20) NOT NULL,
    verification_status VARCHAR(20),
    allergy_type VARCHAR(20),
    category VARCHAR(50),
    criticality VARCHAR(50),
    allergy_code BIGINT NOT NULL REFERENCES codeable_concept.concepts(concept_id) ON DELETE CASCADE,
    onset_date DATE,
    recorded_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE SEQUENCE IF NOT EXISTS clinic_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS patient.clinic (
    id BIGINT PRIMARY KEY DEFAULT nextval('clinic_id_seq'),
    clinicName VARCHAR(30) NOT NULL,
    clinicEmail VARCHAR(30),
    clinicAddress VARCHAR(100)
);


CREATE SEQUENCE IF NOT EXISTS clinic_telecom_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS patient.clinic_telecom (
    id BIGINT PRIMARY KEY DEFAULT nextval('clinic_telecom_id_seq'),
    clinic_id BIGINT NOT NULL,
    telecom_id BIGINT NOT NULL,
    FOREIGN KEY (clinic_id) REFERENCES patient.clinic(id) ON DELETE CASCADE,
    FOREIGN KEY (telecom_id) REFERENCES users.common_telecom(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
