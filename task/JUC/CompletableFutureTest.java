package com.test;

import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.locks.LockSupport;


public class CompletableFutureTest {


    @Test
    public void test1() {
        CompletableFuture<String> message = CompletableFuture.completedFuture("message");

        Assert.assertTrue(message.isDone());
        Assert.assertEquals("message", message.getNow(null));

    }

    @Test
    public void test2() throws InterruptedException {
        CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
            Assert.assertTrue(Thread.currentThread().isDaemon());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {

            }
        });
        Assert.assertFalse(voidCompletableFuture.isDone());
        Thread.sleep(2000);
        Assert.assertTrue(voidCompletableFuture.isDone());
    }

    @Test
    public void test3() {
        CompletableFuture<String> message = CompletableFuture.completedFuture("message").thenApply(s -> {
            Assert.assertFalse(Thread.currentThread().isDaemon());
            return s.toUpperCase();
        });
        Assert.assertEquals("MESSAGE", message.getNow(null));
    }

    @Test
    public void test4() {

        CompletableFuture cf = CompletableFuture.completedFuture("message").thenApplyAsync(s -> {
            Assert.assertTrue(Thread.currentThread().isDaemon());
            LockSupport.parkNanos(200000);

            return s.toUpperCase();


        });
        Assert.assertNull(cf.getNow(null));
        Assert.assertEquals("MESSAGE", cf.join());
    }

    @Test
    public void test5() {
        ExecutorService executorService = Executors.newFixedThreadPool(3, new ThreadFactory() {
            int i = 1;

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "customer" + i++);
            }
        });
        CompletableFuture<String> stringCompletableFuture = CompletableFuture.completedFuture("message").thenApplyAsync(s -> {
            Assert.assertFalse(Thread.currentThread().isDaemon());
            Assert.assertTrue(Thread.currentThread().getName().startsWith("customer"));

            LockSupport.parkNanos(200000);

            return s.toUpperCase();


        }, executorService);

        Assert.assertNull(stringCompletableFuture.getNow(null));
        Assert.assertEquals("MESSAGE", stringCompletableFuture.join());
    }

    @Test
    public void test6() {
        StringBuilder result = new StringBuilder();
        CompletableFuture.completedFuture("thenAccept message")
                .thenAccept(s -> {
                    Assert.assertFalse(Thread.currentThread().isDaemon());
                    result.append(s);
                });
        Assert.assertTrue("Result was empty", result.length() > 0);
    }

    @Test
    public void test7() {
        CompletableFuture<String> message = CompletableFuture.completedFuture("message").thenApplyAsync(s -> {
            LockSupport.parkNanos(20000);
            return s.toUpperCase();
        });
        CompletableFuture<String> exceptionally = message.exceptionally(e -> "canceled message");
        Assert.assertTrue("not cancel", message.cancel(true));
        Assert.assertEquals("canceled message", exceptionally.join());
    }
}
