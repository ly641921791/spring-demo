package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.EnableAsync;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
@EnableRetry
@EnableAsync
@SpringBootApplication
public class SpringBootRetry implements ApplicationRunner {

    @Autowired
    SyncMethodRetry syncMethodRetry;
    @Autowired
    SyncMethodRetryFallback syncMethodRetryFallback;
    @Autowired
    AsyncMethodRetry asyncMethodRetry;
    @Autowired
    AsyncMethodRetryFallback asyncMethodRetryFallback;

    public static void main(String[] args) {
        SpringApplication.run(SpringBootRetry.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        // 同步方法重试
        // syncMethodRetry.retryMethod();

        // 同步方法重试，通过降级成功
        // syncMethodRetryFallback.retryMethod();

        // 异步方法重试
        // asyncMethodRetry.retryMethod();

        // 异步方法重试，通过降级成功
        asyncMethodRetryFallback.retryMethod();
    }

}