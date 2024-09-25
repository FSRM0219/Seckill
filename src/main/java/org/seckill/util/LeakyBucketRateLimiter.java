package org.seckill.util;

import java.util.concurrent.atomic.AtomicInteger;

public class LeakyBucketRateLimiter {
    private final int capacity;
    private final long leakRate;
    private final AtomicInteger currentWater;
    private long lastLeakTime;

    public LeakyBucketRateLimiter(int capacity, int leakRate) {
        this.capacity = capacity;
        this.leakRate = leakRate;
        this.currentWater = new AtomicInteger(0);
        this.lastLeakTime = System.currentTimeMillis();
    }

    public synchronized boolean tryAcquire() {
        long now = System.currentTimeMillis();
        long elapsedTime = now - lastLeakTime;
        int leakedWater = (int) (elapsedTime * leakRate / 1000);
        if (leakedWater > 0) {
            currentWater.set(Math.max(0, currentWater.get() - leakedWater));
            lastLeakTime = now;
        }

        if (currentWater.get() < capacity) {
            currentWater.incrementAndGet();
            return true;
        }
        return false;
    }
}