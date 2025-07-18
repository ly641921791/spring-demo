package org.example;

import jakarta.annotation.Resource;
import org.example.mapper.UserMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootMybatisSqlInjector implements ApplicationRunner {

    @Resource
    private UserMapper userMapper;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootMybatisSqlInjector.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        userMapper.truncateTable();
    }

}
