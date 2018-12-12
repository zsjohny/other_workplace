package com.jfinal.weixin.jiuy.cache;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class PhaserTest {

    public static void arrived() {
        //创建时，就需要指定参与的parties个数
        int parties = 12;
//可以在创建时不指定parties
// 而是在运行时，随时注册和注销新的parties
        Phaser phaser = new Phaser();
//主线程先注册一个
//对应下文中，主线程可以等待所有的parties到达后再解除阻塞（类似与CountDownLatch）
        phaser.register();
        ExecutorService executor = Executors.newFixedThreadPool(parties);
        for (int i = 0; i < parties; i++) {
            phaser.register();//每创建一个task，我们就注册一个party
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        int i = 0;
                        while (i < 3 && !phaser.isTerminated()) {
                            System.out.println("Generation:" + phaser.getPhase());
                            Thread.sleep(3000);
                            //等待同一周期内，其他Task到达
                            //然后进入新的周期，并继续同步进行
                            phaser.arriveAndAwaitAdvance();
                            i++;//我们假定，运行三个周期即可


                            if (i == 1) {

                            }
                        }
                    } catch (Exception e) {

                    } finally {
                        phaser.arriveAndDeregister();
                    }
                }
            });
        }
//主线程到达，且注销自己
//此后线程池中的线程即可开始按照周期，同步执行。
        phaser.arriveAndDeregister();
    }

    public static void main(String[] args) {
        if (1 == 1) {
            arrived();
            return;
        }
        Phaser phaser = new Phaser(2);

        for (int i = 0; i < 5; i++) {
            Task_01 task_01 = new Task_01(phaser);
            Thread thread = new Thread(task_01, "PhaseTest_" + i);
            thread.start();
        }
    }

    static class Task_01 implements Runnable {
        private final Phaser phaser;

        public Task_01(Phaser phaser) {
            this.phaser = phaser;
        }

        @Override
        public void run() {
            System.out.println(Thread.currentThread().getName() + "执行任务完成，等待其他任务执行......");
            //等待其他任务执行完成
            phaser.arriveAndAwaitAdvance();

            System.out.println(Thread.currentThread().getName() + "继续执行任务...");
        }
    }
}
