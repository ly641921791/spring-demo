package example;

import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@Slf4j
@RestController
@SpringBootApplication
public class SpringCloudRateLimitResilience4j {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudRateLimitResilience4j.class, args);
    }

    @RateLimiter(name = "hello-server", fallbackMethod = "helloFallback")
    @RequestMapping
    public String hello() {
        log.info("hello");
        return "Hello World!";
    }

    public String helloFallback(Throwable e) {
        log.error("fallback", e);
        return "fallback";
    }

}