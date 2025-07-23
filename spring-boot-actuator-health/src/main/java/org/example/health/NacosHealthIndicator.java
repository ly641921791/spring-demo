package org.example.health;

import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class NacosHealthIndicator extends AbstractHealthIndicator {

    @Override
    protected void doHealthCheck(Health.Builder builder) throws Exception {
        // 这里模拟Nacos服务状态
        if (new Random().nextInt(100) > 1) {
            builder.up().withDetail("ext1", "extInfo");
        } else {
            builder.down().withDetail("ext2","extInfo");
        }
    }

}
