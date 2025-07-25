package org.example;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfig {

    @Bean
    public Timer apiTimer(MeterRegistry meterRegistry) {
        return Timer.builder("api_timer")
                .description("API耗时")
                .register(meterRegistry);
    }

}
