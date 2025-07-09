package example;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@Slf4j
@RestController
@EnableHystrix
@SpringBootApplication
public class SpringCloudCircuitBreakerHystrix {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudCircuitBreakerHystrix.class, args);
    }

    @HystrixCommand(fallbackMethod = "helloFallback")
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