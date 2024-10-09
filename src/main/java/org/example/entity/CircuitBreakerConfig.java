package org.example.entity;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CircuitBreakerConfig {
    private Long timeWindowSec;
    private Long failureRatioThreshold;
    private Long circuitCloseTimeSec;
    private Integer minRequests;
}
