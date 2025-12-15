CREATE OR REPLACE FUNCTION cleanup_app_data()
RETURNS void AS $$
BEGIN
    -- Delete expired OTPs
    DELETE FROM users.otp
    WHERE expires_at < now();

    -- Delete unverified users older than 10 minutes
    DELETE FROM users.users
    WHERE verified = false
    AND created_at < now() - interval '10 minutes';

    -- Delete expired password reset tokens
    DELETE FROM users.password_reset_token
    WHERE expiry_date < now();

    -- Delete patients without users
    DELETE FROM patient.patient p
    WHERE NOT EXISTS (
        SELECT 1 FROM users u WHERE u.id = p.user_id
    );
END;
$$ LANGUAGE plpgsql;
