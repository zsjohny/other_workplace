package com.finace.miscroservice.task_scheduling.test;

import java.util.List;

public class TaskImpl implements Task<String> {
    @Override
    public void process(List<String> tasks) {
        tasks.forEach(System.out::println);
        System.out.println("end");
    }
}
