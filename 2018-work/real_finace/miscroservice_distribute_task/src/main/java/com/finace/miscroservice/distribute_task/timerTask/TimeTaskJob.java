package com.finace.miscroservice.distribute_task.timerTask;


import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Collection;

public abstract class TimeTaskJob implements Job {


    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {

        Collection<Object> values = jobExecutionContext.getMergedJobDataMap().values();
        TimeTask task;

        for (Object obj : values.toArray()) {
            task = (TimeTask) obj;
            if (task.getParams() == null || task.getParams().isEmpty()) {
                continue;
            }

            job(task.getParams());


        }
    }

    public abstract void job(String params);


}
