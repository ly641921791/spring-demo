package org.example;

import lombok.extern.slf4j.Slf4j;

import java.util.stream.IntStream;

/**
 * 滑动窗口限流算法实现
 * 只是为了简单实现算法，时间窗口单位为秒
 */
@Slf4j
public class SlidingWindowLimiter {

    /**
     * 当前时间窗口
     */
    private long timeWindow;
    /**
     * 每个时间窗口可通过请求数
     */
    private int maxRequestCount;
    /**
     * 当前时间窗口已通过请求数，每个时间窗口分为10个子窗口滑动
     */
    private int[] currentRequestCount = new int[10];

    /**
     * 简单实现一个固定窗口限流算法
     *
     * @param maxRequestCount 每秒可通过请求
     */
    public SlidingWindowLimiter(int maxRequestCount) {
        this.maxRequestCount = maxRequestCount;
    }

    public boolean acquire() {
        // 每个时间（单位：秒）窗口分为10个子窗口滑动，所以除以100的得到当前的时间窗口，对10取余得到时间窗口下标
        long currentTime = System.currentTimeMillis() / 100;
        int currentWindow = (int) (currentTime % 10);

        // 重置时间窗口，这里只考虑连续时间窗口都有请求的情况，不考虑跳过时间窗口请求的情况
        if (currentTime != timeWindow) {
            currentRequestCount[currentWindow] = 0;
            timeWindow = currentTime;
        }

        int sum = IntStream.of(currentRequestCount).sum();
        // 判断是否限流
        if (sum >= maxRequestCount) {
            return false;
        }

        currentRequestCount[currentWindow]++;
        return true;
    }

    public static void main(String[] args) throws InterruptedException {
        SlidingWindowLimiter fixedWindowLimiter = new SlidingWindowLimiter(10);
        for (int i = 0; i < 100; i++) {
            Thread.sleep(40);
            if (fixedWindowLimiter.acquire()) {
                log.info("通过");
            } else {
                log.info("限流");
            }
        }
    }

}
