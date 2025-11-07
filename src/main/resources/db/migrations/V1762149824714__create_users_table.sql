CREATE SEQUENCE IF NOT EXISTS users_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS users.users (
    id BIGINT PRIMARY KEY DEFAULT nextval('users_id_seq'),
    email VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE IF NOT EXISTS users.roles (
   id SERIAL PRIMARY KEY,
   name VARCHAR(50) UNIQUE NOT NULL
);


CREATE TABLE IF NOT EXISTS users.user_roles (
     user_id BIGINT NOT NULL REFERENCES users.users(id) ON DELETE CASCADE,
     role_id BIGINT NOT NULL REFERENCES users.roles(id) ON DELETE CASCADE,
     PRIMARY KEY (user_id, role_id)
);


CREATE SEQUENCE IF NOT EXISTS otp_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS users.otp (
    id BIGINT PRIMARY KEY DEFAULT nextval('otp_id_seq'),
    user_id BIGINT NOT NULL REFERENCES users.users(id),
    otp VARCHAR(10) NOT NULL,
    expires_at TIMESTAMP NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE SEQUENCE IF NOT EXISTS common_address_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS users.common_address (
    id BIGINT PRIMARY KEY DEFAULT nextval('common_address_id_seq'),
    use_code VARCHAR(20),
    address_type VARCHAR(50),
    text VARCHAR(255),
    line1 VARCHAR(100),
    line2 VARCHAR(100),
    city VARCHAR(50),
    district VARCHAR(50),
    state VARCHAR(50),
    postal_code VARCHAR(20),
    country VARCHAR(50),
    period_start DATE,
    period_end DATE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);


CREATE SEQUENCE IF NOT EXISTS common_telecom_id_seq START WITH 1;
CREATE TABLE IF NOT EXISTS users.common_telecom (
    id BIGINT PRIMARY KEY DEFAULT nextval('common_telecom_id_seq'),
    system VARCHAR(20) NOT NULL,
    value VARCHAR(100) NOT NULL,
    use_code VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
