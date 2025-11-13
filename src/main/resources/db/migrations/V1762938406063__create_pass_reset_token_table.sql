CREATE TABLE IF NOT EXISTS users.password_reset_token (
    id BIGSERIAL PRIMARY KEY,
    token VARCHAR(255) NOT NULL UNIQUE,
    user_id BIGINT NOT NULL,
    expiry_date TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users.users(id) ON DELETE CASCADE
);

CREATE SEQUENCE IF NOT EXISTS users.password_reset_token_id_seq;