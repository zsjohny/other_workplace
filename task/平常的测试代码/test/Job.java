package com.finace.miscroservice.task_scheduling.test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Job<Item> {
    private ScheduledExecutorService checkTimeOut;
    private Compare<Item> compare;

    public  Job( int bufferSize,  long waitTime, Task<Item> task) {
        compare = new Compare<>( bufferSize,  waitTime, task);
        new Thread(compare).start();
        checkTimeOut = Executors.newScheduledThreadPool(1);
        checkTimeOut.scheduleAtFixedRate(compare::isTimeOut, 1000, 1000, TimeUnit.MICROSECONDS);
    }

    public void add(Item item) {
        compare.add(item);

    }


}
