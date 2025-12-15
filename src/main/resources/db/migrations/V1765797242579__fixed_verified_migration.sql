-- Fix index to use correct column name

DROP INDEX IF EXISTS idx_users_users_verified_created_at;

CREATE INDEX IF NOT EXISTS idx_users_users_is_verified_created_at
ON users.users (is_verified, created_at);
