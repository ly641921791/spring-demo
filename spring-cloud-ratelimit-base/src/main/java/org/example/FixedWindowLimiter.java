package org.example;

import lombok.extern.slf4j.Slf4j;

/**
 * 固定窗口限流算法实现
 * 只是为了简单实现算法，时间窗口单位为秒
 */
@Slf4j
public class FixedWindowLimiter {

    /**
     * 当前时间窗口
     */
    private long timeWindow;
    /**
     * 每个时间窗口可通过请求数
     */
    private int maxRequestCount;
    /**
     * 当前时间窗口已通过请求数
     */
    private int currentRequestCount;

    /**
     * 简单实现一个固定窗口限流算法
     *
     * @param maxRequestCount 每秒可通过请求
     */
    public FixedWindowLimiter(int maxRequestCount) {
        this.maxRequestCount = maxRequestCount;
    }

    public boolean acquire() {
        long currentTime = System.currentTimeMillis() / 1000;

        // 重置时间窗口
        if (currentTime != timeWindow) {
            currentRequestCount = 0;
            timeWindow = currentTime;
        }

        // 判断是否限流
        if (currentRequestCount >= maxRequestCount) {
            return false;
        }

        currentRequestCount++;
        return true;
    }

    public static void main(String[] args) throws InterruptedException {
        FixedWindowLimiter fixedWindowLimiter = new FixedWindowLimiter(10);
        for (int i = 0; i < 100; i++) {
            Thread.sleep(8);
            if (fixedWindowLimiter.acquire()) {
                log.info("通过");
            } else {
                log.info("限流");
            }
        }
    }

}
