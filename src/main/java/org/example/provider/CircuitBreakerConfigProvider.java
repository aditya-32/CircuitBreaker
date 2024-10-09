package org.example.provider;

import com.google.inject.Inject;
import com.google.inject.Provider;
import lombok.RequiredArgsConstructor;
import org.example.entity.CircuitBreakerConfig;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class CircuitBreakerConfigProvider implements Provider<CircuitBreakerConfig> {
    @Override
    public CircuitBreakerConfig get() {
        return CircuitBreakerConfig.builder()
                .minRequests(3)
                .circuitCloseTimeSec(2L)
                .failureRatioThreshold(50L)
                .timeWindowSec(5L)
                .build();
    }
}
