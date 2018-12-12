package com.goldplusgold.td.market.common;

import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.StampedLock;

public class LockSupportTest1 {


    public static class TestStampedLock {
        final static StampedLock lock = new StampedLock();

        public static  void test() throws InterruptedException {

            new Thread(){

                public void run(){

                    long readLong = lock.writeLock();

                    LockSupport.parkNanos(6100000000L);

                    lock.unlockWrite(readLong);

                }

            }.start();

            Thread.sleep(100);

            for( int i = 0; i < 3; ++i)

                new Thread(new DateTimeUtil.TestStampedLock.OccupiedCPUReadThread(lock)).start();
        }

        private static class OccupiedCPUReadThread implements Runnable{

            private StampedLock lock;

            public OccupiedCPUReadThread(StampedLock lock){

                this.lock = lock;

            }

            public void run(){

//                Thread.currentThread().interrupt();

                long lockr = lock.readLock();

                System.out.println(Thread.currentThread().getName() + " get read lock");

                lock.unlockRead(lockr);

            }

        }

    }

}

因为实现等待的那部分逻辑在一个循环里，里面有一个LockSupport.pack来实现等待，满足条件后才跳出循环，结束等待，但如果线程处于中断状态，LockSupport.pack不会开始等待或继续等待，而且也不会清除线程的中断状态，所以造成了在循环里无限调用
LockSupport.pack（pack总是立即返回）的情形，所以cpu就满负荷了