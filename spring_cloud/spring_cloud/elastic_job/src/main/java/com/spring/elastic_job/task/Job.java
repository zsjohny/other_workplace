package com.spring.elastic_job.task;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

public abstract class Job implements SimpleJob {
    @Override
    public void execute(ShardingContext shardingContext) {
        ;
        System.out.println(shardingContext.getJobParameter());
        task(shardingContext.getShardingParameter());

    }

    public abstract void task(String taskInfo);
}
