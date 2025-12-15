package com.example.demo.cleanup;

import java.sql.SQLException;

import org.flywaydb.core.internal.jdbc.JdbcTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CleanupRunner {

    private final JdbcTemplate jdbcTemplate;

    public void runCleanup() throws SQLException {
        jdbcTemplate.execute("SELECT cleanup_app_data()");
    }
}
