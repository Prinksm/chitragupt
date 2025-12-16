package com.example.demo.emails.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class ResendEmailClient {

    @Value("${resend.api.key}")
    private String apiKey;

    @Value("${resend.from}")
    private String from;

    @Value("${resend.forward.inbox}")
    private String forwardInbox;

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void send(String to, String subject, String html) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("from", from);

            // ALWAYS send to your inbox
            payload.put("to", Collections.singletonList(forwardInbox));

            // Encode real recipient in subject
            payload.put("subject", "[TO:" + to + "] " + subject);
            payload.put("html", html);

            String json = objectMapper.writeValueAsString(payload);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.resend.com/emails"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            HttpResponse<String> response = httpClient.send(
                    request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 300) {
                throw new RuntimeException("Resend API error: " + response.body());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email via Resend", e);
        }
    }

}
