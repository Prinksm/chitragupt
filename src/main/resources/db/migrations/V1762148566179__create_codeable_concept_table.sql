CREATE SEQUENCE IF NOT EXISTS code_system_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS codeable_concept.code_system (
    system_id BIGINT PRIMARY KEY DEFAULT nextval('code_system_id_seq'),
    system_name VARCHAR(255) NOT NULL UNIQUE,
    version VARCHAR(50)
);


CREATE SEQUENCE IF NOT EXISTS concept_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS codeable_concept.concepts (
    concept_id BIGINT PRIMARY KEY DEFAULT nextval('concept_id_seq'),
    concept_name VARCHAR(255) NOT NULL,
    description VARCHAR(255),
    type VARCHAR(255) NOT NULL
);


CREATE TABLE IF NOT EXISTS codeable_concept.concept_codings_mapping (
    concept_id BIGINT NOT NULL,
    system_id BIGINT NOT NULL,
    code VARCHAR(255) NOT NULL PRIMARY KEY,
    display_text VARCHAR(255),
    FOREIGN KEY (concept_id) REFERENCES codeable_concept.concepts(concept_id),
    FOREIGN KEY (system_id) REFERENCES codeable_concept.code_system(system_id)
);
