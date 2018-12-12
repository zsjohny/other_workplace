package com.finace.miscroservice.task_scheduling.test;

import java.util.List;

public interface Task<Item> {

    void process(List<Item> tasks);
}
