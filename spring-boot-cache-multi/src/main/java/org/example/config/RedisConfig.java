package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.embedded.RedisServer;

@Configuration
public class RedisConfig {

    @Bean(initMethod = "start", destroyMethod = "stop")
    RedisServer redisServer() {
        return RedisServer.builder().build();
    }

}
