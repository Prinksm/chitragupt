package com.example.demo.cleanup;

import java.sql.SQLException;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CleanupRunner {

    private final JdbcTemplate jdbcTemplate;

    public void runCleanup() throws SQLException {
        jdbcTemplate.execute("SELECT cleanup_app_data()");
    }
}
