package com.jfinal.weixin.jiuy.cache;

import java.util.concurrent.Phaser;

public class OwnerTest {

    public static void main(String[] args) {
        Phaser phaser = new Phaser(4);


        new Thread(() -> {

            phaser.register();

            try {
                Thread.sleep(3000);
                System.out.println("_______________________");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            phaser.arriveAndDeregister();
        }

        ).start();


        for (int i = 0; i < 3; i++) {
            int k = i;
            new Thread(() -> {
                System.out.println("__" + k);
                phaser.arriveAndDeregister();
                System.out.println("____" + k);
            }).start();


        }

        for (int i = 0; i < 3; i++) {
            int k = i;
            new Thread(() -> {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {

                }
                System.out.println("做东西" + k);
                phaser.arrive();
            }).start();

            new Thread(() -> {
                phaser.awaitAdvance(phaser.getPhase());
                System.out.println("吃东西" + k);
            }).start();
        }


    }

}
