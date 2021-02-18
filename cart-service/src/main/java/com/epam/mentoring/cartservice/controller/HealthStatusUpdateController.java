package com.epam.mentoring.cartservice.controller;

import com.epam.mentoring.cartservice.healthindicator.CustomHealthIndicator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/status")
public class HealthStatusUpdateController {
    @Autowired
    private CustomHealthIndicator customHealthIndicator;

    @PostMapping
    public ResponseEntity<String> addProduct(@RequestBody String healthStatus) {
        customHealthIndicator.setHealthStatus(Health.status(healthStatus).build());
        return ResponseEntity.ok(healthStatus);
    }
}
