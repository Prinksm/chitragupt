package com.example.demo.patientAllergy;

import java.util.Base64;

public class AllergySlugUtil {
    private static final String PREFIX = "alg";

    public static String toSlug(Long allergyId) {
        String encoded = Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(allergyId.toString().getBytes());
        return PREFIX + "-" + encoded;
    }

    public static Long fromSlug(String slug) {
        if (!slug.startsWith(PREFIX + "-")) {
            throw new IllegalArgumentException("Invalid allergy slug");
        }

        String encoded = slug.substring(PREFIX.length() + 1);
        String decoded = new String(
                Base64.getUrlDecoder().decode(encoded)
        );

        return Long.parseLong(decoded);
    }
}