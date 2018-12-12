package com.finace.miscroservice.task_scheduling.test;

import java.util.List;

public interface Processor<T> {

    void process(List<T> list);

}