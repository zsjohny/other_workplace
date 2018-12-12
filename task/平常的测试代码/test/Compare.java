package com.finace.miscroservice.task_scheduling.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.LockSupport;

public class Compare<Item> implements Runnable {

    public Compare(int bufferSize, long waitTime, Task<Item> task) {
        this.bufferSize = bufferSize;
        this.waitTime = waitTime;
        this.task = task;
        this.lastSubmitTime = System.currentTimeMillis();
        taskCache = new LinkedBlockingDeque<>(bufferSize);
    }

    private BlockingQueue<Item> taskCache;


    private int bufferSize;

    private long lastSubmitTime;

    private long waitTime;
    private Task<Item> task;

    private Thread myThread;

    public void add(Item item) {
        taskCache.add(item);
        enabledTrigger();
    }

    private void enabledTrigger() {

        if (taskCache.size() >= bufferSize) {
            start();
        } else {
            isTimeOut();
        }

    }

    public void isTimeOut() {
        if (System.currentTimeMillis() - lastSubmitTime >= waitTime) {
            start();
        }
    }

    private void start() {
        LockSupport.unpark(myThread);
    }


    @Override
    public void run() {
        myThread = Thread.currentThread();

        while (!myThread.isInterrupted()) {
            if (taskCache.size() < bufferSize) {
                LockSupport.park(this);
            }
            System.out.println("____________");

            work();
        }
    }

    private void work() {
        if (taskCache.size() == 0) {
            return;
        }
        List<Item> list = new ArrayList<>(bufferSize);
        int transferSize = taskCache.drainTo(list, bufferSize);
        if (transferSize > 0) {
            task.process(list);
            System.out.println("do task");
            lastSubmitTime = System.currentTimeMillis();
        }
    }
}
