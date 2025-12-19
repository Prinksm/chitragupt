package com.example.demo.LocationLookup;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/location")
public class LocationLookupController {
    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/postal/{countryCode}/{postalCode}")
    public ResponseEntity<?> lookupByPostalCode(
            @PathVariable String countryCode,
            @PathVariable String postalCode) {
        try {
            String url = "https://api.zippopotam.us/" + countryCode + "/" + postalCode;

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null || !response.containsKey("places")) {
                return ResponseEntity.badRequest()
                        .body("Invalid postal code");
            }

            List<Map<String, Object>> places = (List<Map<String, Object>>) response.get("places");

            if (places.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("No location found");
            }

            Map<String, Object> place = places.get(0);

            Map<String, String> result = new HashMap<>();
            result.put("city", (String) place.get("place name"));
            result.put("state", (String) place.get("state"));
            result.put("country", countryCode);

            return ResponseEntity.ok(result);

        } catch (HttpClientErrorException.NotFound ex) {
            return ResponseEntity.badRequest()
                    .body("Invalid postal code for country");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Location lookup failed");
        }
    }

    public static class LocationLookupService {
    }
}