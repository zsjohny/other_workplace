package org.finace.schedule.utils;

import org.finace.schedule.entity.TimeTaskJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 定时任务的总工具类
 * Created by Ness on 2016/12/10.
 */
@Component
public class TimeTaskBus {
    private Logger logger = LoggerFactory.getLogger(TimeTaskBus.class);
    private static String JOB_GROUP = "job_group";
    private static String SCHEDULE_NAME = "job_group";

    @PostConstruct
    private void initTask() throws SchedulerException {
        System.out.println("1111111");
        SchedulerFactory factory = new StdSchedulerFactory();
        Scheduler scheduler = factory.getScheduler();

        String cronExpression = "*/5 * * * * ?";

        JobDetail job = JobBuilder.newJob(TimeTaskJob.class).withIdentity(SCHEDULE_NAME, JOB_GROUP).build();
        job.getJobDataMap().put("11", "222");

        CronScheduleBuilder cron = CronScheduleBuilder.cronSchedule(cronExpression);
        Trigger trigger = TriggerBuilder.newTrigger().withIdentity(SCHEDULE_NAME, JOB_GROUP).withSchedule(cron).startNow().build();
        trigger.getJobDataMap().put("111", "222gggg");
        scheduler.scheduleJob(job, trigger);

        scheduler.start();

          /*    JobKey jobKey = JobKey.jobKey("11", JOB_GROUP);
      scheduler.pauseJob(jobKey);
        scheduler.resumeJob(jobKey);
        scheduler.deleteJob(jobKey);
        scheduler.triggerJob(jobKey);*/
    }

    public static void main(String[] args) throws SchedulerException {
        TimeTaskBus bus = new TimeTaskBus();
        bus.initTask();

    }
}
