package com.example.demo.ShareToken;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ShareTokenService {
    private static final long EXPIRY_MS = 10 * 60 * 1000; // 10 minutes

    private final Map<String, TokenEntry> store = new ConcurrentHashMap<>();

    public String createToken(Long patientId, String email) {
        String token = UUID.randomUUID().toString();
        store.put(token, new TokenEntry(
                patientId,
                email,
                System.currentTimeMillis() + EXPIRY_MS
        ));
        return token;
    }

    public TokenEntry validate(String token, String email) {
        TokenEntry entry = store.get(token);

        if (entry == null) return null;
        if (!entry.email().equals(email)) return null;
        if (System.currentTimeMillis() > entry.expiresAt()) {
            store.remove(token);
            return null;
        }
        return entry;
    }

    // 🔁 Rotate token (refresh)
    public String rotate(String oldToken) {
        TokenEntry entry = store.remove(oldToken);
        if (entry == null) return null;

        String newToken = UUID.randomUUID().toString();
        store.put(newToken, new TokenEntry(
                entry.patientId(),
                entry.email(),
                System.currentTimeMillis() + EXPIRY_MS
        ));
        return newToken;
    }

    public record TokenEntry(Long patientId, String email, long expiresAt) {}

}
