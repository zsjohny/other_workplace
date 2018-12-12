package com.finace.miscroservice.task_scheduling.test;

public class TestTask {
    public static void main(String[] args) throws InterruptedException {
        Job<String> job = new Job(5, 2000, new TaskImpl());
        for (int i = 0; i < 200; i++) {
            job.add(String.valueOf(i));
            Thread.sleep(6000);
        }
    }
}
