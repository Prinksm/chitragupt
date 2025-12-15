package com.example.demo.cleanup;

import java.sql.SQLException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/internal")
@RequiredArgsConstructor
public class CleanupController {

    private final CleanupRunner cleanupRunner;

    @PostMapping("/cleanup")
    public ResponseEntity<String> cleanup(
            @RequestHeader("X-CRON-KEY") String key) throws SQLException {

        if (!key.equals(System.getenv("CRON_SECRET"))) {
            return ResponseEntity.status(403).build();
        }

        cleanupRunner.runCleanup();
        return ResponseEntity.ok("Cleanup completed");
    }
}
