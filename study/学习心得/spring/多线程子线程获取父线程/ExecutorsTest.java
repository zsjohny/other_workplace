package com.test.config;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author nessary
 */
public class ExecutorsTest {

    public static void main(String[] args) {

        final ScheduledExecutorService scheduler =
                Executors.newScheduledThreadPool(1);

        final Runnable beeper1 = new Runnable() {
            public void run() {
                System.out.println("beep1");
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        final Runnable beeper2 = new Runnable() {
            public void run() {
                System.out.println("beep2");
                try {
                    Thread.sleep(10 * 1000);
                } catch (InterruptedException e) {

                }
            }
        };

        final ScheduledFuture<?> beeperHandle1 =
                scheduler.scheduleAtFixedRate(beeper1, 5, 5, SECONDS);
        final ScheduledFuture<?> beeperHandle2 =
                scheduler.scheduleWithFixedDelay(beeper2, 5, 5, SECONDS);

    }
}


