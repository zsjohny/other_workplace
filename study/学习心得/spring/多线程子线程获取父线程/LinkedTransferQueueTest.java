package com.test.config.controller;


import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.LinkedTransferQueue;

public class LinkedTransferQueueTest {

   
    public static void main(String[] args) throws InterruptedException {
        LinkedTransferQueue queue = new LinkedTransferQueue();

        new Thread(() -> {

            while (true) {

                //是否有消费者
//                if (queue.hasWaitingConsumer()) {
                    try {
                        //取出数据 一直等待 这个被消费结束 才会下面添加
                        queue.transfer("111");
                        System.out.println("____");
                    } catch (InterruptedException e) {

                    }
//                }
            }

        }).start();


        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Object take = queue.take();
                    System.out.println(
                            take
                    );
                    Thread.sleep(1000);
                } catch (InterruptedException e) {

                }
            }
        }).start();


    }

}
