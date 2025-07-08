package org.example;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AsyncMethodRetryFallback {
    int i = 0;

    @Async
    @Retryable(
            // 指定降级方法，只有一个降级方法可不指定
            recover = "fallbackMethod",
            // 重试的异常类型
            retryFor = Exception.class,
            // 最大重试次数
            maxAttempts = 3,
            // 重试初始延迟和延迟倍数
            backoff = @Backoff(delay = 1000, multiplier = 2)
    )
    public void retryMethod() {
        try {
            log.info("第{}次执行", i + 1);
            throw new RuntimeException("触发重试");
        } finally {
            i++;
        }
    }

    @Recover
    public void fallbackMethod() {
        log.info("降级成功1");
    }

    @Recover
    public void fallbackMethod2() {
        log.info("降级成功2");
    }

}
