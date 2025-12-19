package com.example.demo.LocationLookup;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LocationLookupService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Cacheable(value = "postalLookup", key = "#countryCode + '-' + #postalCode")
    public Map<String, String> lookupByPostalCode(String countryCode, String postalCode) {

        try {
            String url = "https://api.zippopotam.us/" + countryCode + "/" + postalCode;

            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response == null || !response.containsKey("places")) {
                throw new RuntimeException("Invalid postal code");
            }

            List<Map<String, Object>> places = (List<Map<String, Object>>) response.get("places");

            if (places.isEmpty()) {
                throw new RuntimeException("No location found");
            }

            Map<String, Object> place = places.get(0);

            Map<String, String> result = new HashMap<>();
            result.put("city", (String) place.get("place name"));
            result.put("state", (String) place.get("state"));
            result.put("country", countryCode);

            return result;

        } catch (HttpClientErrorException.NotFound ex) {
            throw new RuntimeException("Invalid postal code for country");
        } catch (Exception ex) {
            throw new RuntimeException("Location lookup failed");
        }
    }
}