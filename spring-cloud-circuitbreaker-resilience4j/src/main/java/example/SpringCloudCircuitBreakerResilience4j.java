package example;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@Slf4j
@RestController
@SpringBootApplication
public class SpringCloudCircuitBreakerResilience4j {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudCircuitBreakerResilience4j.class, args);
    }

    @CircuitBreaker(name = "hello-server", fallbackMethod = "helloFallback")
    @RequestMapping
    public String hello() {
        int i = new Random().nextInt(10);
        if (i > 3) {
            throw new RuntimeException("error");
        }
        log.info("hello");
        return "Hello World!";
    }

    public String helloFallback(Throwable e) {
        log.error("fallback", e);
        return "fallback";
    }

}