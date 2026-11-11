package com.formgenius.common;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;

@RestController
@Tag(name = "Health", description = "Application health status")
public class HealthController {

    @GetMapping({"/api/health", "/api/v1/health"})
    @Operation(summary = "Health check")
    public ResponseEntity<Map<String, Object>> getHealth() {
        return ResponseEntity.ok(Map.of(
                "status", "UP",
                "service", "Timely Forms AI",
                "version", "1.0.0",
                "timestamp", Instant.now().toString()
        ));
    }
}
