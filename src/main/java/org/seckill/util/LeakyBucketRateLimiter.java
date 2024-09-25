package org.seckill.util;

import java.util.concurrent.atomic.AtomicInteger;

public class LeakyBucketRateLimiter {
    private final int capacity; // 桶的容量
    private final long leakRate; // 漏水速率（每毫秒漏出的请求数）
    private final AtomicInteger currentWater; // 当前桶中的水量
    private long lastLeakTime; // 上次漏水的时间

    public LeakyBucketRateLimiter(int capacity, int leakRate) {
        this.capacity = capacity;
        this.leakRate = leakRate;
        this.currentWater = new AtomicInteger(0);
        this.lastLeakTime = System.currentTimeMillis();
    }

    public synchronized boolean tryAcquire() {
        long now = System.currentTimeMillis();
        // 计算漏水量
        long elapsedTime = now - lastLeakTime;
        int leakedWater = (int) (elapsedTime * leakRate / 1000);
        if (leakedWater > 0) {
            currentWater.set(Math.max(0, currentWater.get() - leakedWater));
            lastLeakTime = now;
        }

        // 检查是否可以获取请求
        if (currentWater.get() < capacity) {
            currentWater.incrementAndGet();
            return true;
        }
        return false;
    }
}