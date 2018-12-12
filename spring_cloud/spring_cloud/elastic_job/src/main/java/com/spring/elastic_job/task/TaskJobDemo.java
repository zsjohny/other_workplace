package com.spring.elastic_job.task;


public class TaskJobDemo extends Job {


    @Override
    public void task(String taskInfo) {
        System.out.println(taskInfo);
    }
}
