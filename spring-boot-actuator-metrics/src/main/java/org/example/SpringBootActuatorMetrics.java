package org.example;

import io.micrometer.core.instrument.Timer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class SpringBootActuatorMetrics {

    @Autowired
    private Timer timer;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootActuatorMetrics.class, args);
    }

    @RequestMapping
    public String hello() {
        return timer.record(() -> "hello");
    }

}
