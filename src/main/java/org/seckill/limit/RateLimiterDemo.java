package org.seckill.limit;

import org.seckill.util.LeakyBucketRateLimiter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RateLimiterDemo {
    public static void main(String[] args) {
        LeakyBucketRateLimiter rateLimiter = new LeakyBucketRateLimiter(10, 1);
        List<Runnable> tasks = new ArrayList<Runnable>();
        for (int i = 0; i < 10; i++) {
            tasks.add(new UserRequest(i));
        }
        ExecutorService threadPool = Executors.newCachedThreadPool();
        for (Runnable runnable : tasks) {
            System.out.println("等待时间：" + rateLimiter.tryAcquire());
            threadPool.execute(runnable);
        }
    }

    private static class UserRequest implements Runnable {

        private final int id;

        public UserRequest(int id) {
            this.id = id;
        }

        public void run() {
            System.out.println(id);
        }
    }
}
