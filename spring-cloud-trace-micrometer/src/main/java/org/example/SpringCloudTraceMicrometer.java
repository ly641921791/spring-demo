package org.example;

import io.micrometer.tracing.Tracer;
import io.micrometer.tracing.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@Slf4j
@RestController
@SpringBootApplication
public class SpringCloudTraceMicrometer {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudTraceMicrometer.class, args);
    }

    @Autowired
    DbService dbService;

    @Bean
    NewSpanParser newSpanParser() {
        return new DefaultNewSpanParser();
    }
    @Bean
    MethodInvocationProcessor methodInvocationProcessor(NewSpanParser newSpanParser, Tracer tracer, BeanFactory beanFactory) {
        return new ImperativeMethodInvocationProcessor(newSpanParser, tracer, beanFactory::getBean, beanFactory::getBean);
    }

    @Bean
    SpanAspect spanAspect(MethodInvocationProcessor methodInvocationProcessor) {
        return new SpanAspect(methodInvocationProcessor);
    }

    @RequestMapping
    public String hello() {
        log.info("hello");
        dbService.db();
        return "Hello World!";
    }

}