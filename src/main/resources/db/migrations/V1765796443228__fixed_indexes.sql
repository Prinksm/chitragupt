-- OTP expiry lookup
CREATE INDEX IF NOT EXISTS idx_users_otp_expires_at
ON users.otp(expires_at);

-- Unverified users cleanup
CREATE INDEX IF NOT EXISTS idx_users_users_verified_created_at
ON users.users(verified, created_at);

-- Password reset token expiry
CREATE INDEX IF NOT EXISTS idx_users_password_reset_token_expiry
ON users.password_reset_token(expiry_date);

-- Orphan patients
CREATE INDEX IF NOT EXISTS idx_patient_patient_user_id
ON patient.patient(user_id);