package org.example;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@SpringBootApplication
public class SpringCloudTraceSkywalking {

    @Autowired
    DbService dbService;

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudTraceSkywalking.class, args);
    }

    @RequestMapping("/")
    public String hello() {
        log.info("hello");
        dbService.db();
        return "Hello World!";
    }

}