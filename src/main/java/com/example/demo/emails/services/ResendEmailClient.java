package com.example.demo.emails.services;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ResendEmailClient {

    @Value("${resend.api.key}")
    private String apiKey;

    @Value("${resend.from}")
    private String from;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public void send(String to, String subject, String html) {
        try {
            String payload = """
                    {
                      "from": "%s",
                      "to": ["%s"],
                      "subject": "%s",
                      "html": "%s"
                    }
                    """.stripIndent().formatted(
                    escape(from),
                    escape(to),
                    escape(subject),
                    escape(html));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.resend.com/emails"))
                    .header("Authorization", "Bearer " + apiKey)
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 300) {
                throw new RuntimeException("Resend error: " + response.body());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to send email via Resend", e);
        }
    }

    private String escape(String text) {
        return text
                .replace("\\", "\\\\")
                .replace("\"", "\\\"");
    }
}
