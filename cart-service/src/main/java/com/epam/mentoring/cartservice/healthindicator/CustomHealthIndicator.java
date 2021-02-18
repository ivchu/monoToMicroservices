package com.epam.mentoring.cartservice.healthindicator;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {
    private Health healthStatus = Health.down().build();

    @Override
    public Health health() {
        return healthStatus;
    }

    public void setHealthStatus(Health healthStatus) {
        this.healthStatus = healthStatus;
    }

    public Health getHealthStatus() {
        return healthStatus;
    }
}
