-- OTP expiry lookup
CREATE INDEX IF NOT EXISTS idx_otp_expires_at
ON otp(expires_at);

-- Unverified users cleanup
CREATE INDEX IF NOT EXISTS idx_users_verified_created_at
ON users(verified, created_at);

-- Password reset token expiry
CREATE INDEX IF NOT EXISTS idx_password_reset_token_expiry
ON password_reset_token(expiry_date);

-- Orphan patients
CREATE INDEX IF NOT EXISTS idx_patient_user_id
ON patient(user_id);
