package com.finace.miscroservice;


import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class RateMeterTest {

    AtomicInteger atomicInteger = new AtomicInteger();
    RateLimiter limiter = RateLimiter.create(10);

    public void send() {
        limiter.acquire();

        System.out.println("into.." + atomicInteger.getAndIncrement());
    }

    public static void main(String[] args) {
        RateMeterTest rateLimiter = new RateMeterTest();
        for (int i = 0; i < 100; i++) {
            rateLimiter.send();
        }
    }


}
