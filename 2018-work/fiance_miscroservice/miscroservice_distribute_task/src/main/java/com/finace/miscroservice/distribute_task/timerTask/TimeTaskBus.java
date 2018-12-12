package com.finace.miscroservice.distribute_task.timerTask;


import com.finace.miscroservice.distribute_task.util.LogUtil;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.spi.JobFactory;
import org.quartz.spi.TriggerFiredBundle;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 定时任务的总工具类
 */
public class TimeTaskBus {


    private LogUtil logger;

    private String JOB_DETAIL_GROUP = "job_detail_group";
    private String JOB_TRIGGER_GROUP = "job_trigger_group";


    private TimeTaskJob timeTaskJob;


    public TimeTaskBus(TimeTaskJob timeTaskJob, LogUtil logUtil) {
        this.timeTaskJob = timeTaskJob;
        try {
            logger = logUtil.getClass().newInstance();
            logger.setInstance(TimeTaskBus.class);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private Scheduler schedule;

    private volatile JobDetail jobDetail;
    private ReentrantLock scheduleLock = new ReentrantLock();
    private ReentrantLock jobDetailLock = new ReentrantLock();


    {

        try {
            SchedulerFactory factory = new StdSchedulerFactory();
            schedule = factory.getScheduler();


            /**
             * 初始化设置timeTask中的注入类
             */
            schedule.setJobFactory(new JobFactory() {

                @Override
                public Job newJob(TriggerFiredBundle bundle, Scheduler scheduler) throws SchedulerException {

                    if (bundle.getJobDetail().getJobClass().equals(TimeTaskJob.class)) {
                        return timeTaskJob;
                    }
                    try {
                        return bundle.getJobDetail().getJobClass().newInstance();
                    } catch (InstantiationException e) {

                    } catch (IllegalAccessException e) {

                    }
                    return null;
                }
            });
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 初始化定时任务列表
     *
     * @param jobName 定时任务的组名称
     */
    private void initSchedule(String jobName) {
        if (jobName == null || jobName.isEmpty()) {
            return;
        }

        try {
            JobKey jobKey = new JobKey(jobName, JOB_DETAIL_GROUP);
            jobDetailLock.lock();
            jobDetail = schedule.getJobDetail(jobKey);
            if (jobDetail == null) {
                jobDetail = JobBuilder.newJob(TimeTaskJob.class).withIdentity(jobName, JOB_DETAIL_GROUP).build();
            }

        } catch (SchedulerException e) {
            logger.error("初始化定时任务列表失败", e);
        } finally {
            if (jobDetailLock.isLocked()) {
                jobDetailLock.unlock();
            }
        }
    }


    /**
     * 初始化定时任务
     */
    public synchronized void init(List<TimeTask> timeTasks) {
        logger.info("开始实例定时任务");
        initTask(timeTasks);

    }

    private int DEFAULT_RATE_LIMITER_COUNT = 1;
    private Semaphore limiter = new Semaphore(DEFAULT_RATE_LIMITER_COUNT);


    private void initTask(List<TimeTask> timeTasks) {
        logger.info("开始初始化定时任务...");
        try {

            if (timeTasks == null || timeTasks.isEmpty()) {
                logger.info("未检测到需要添加的定时任务");
                return;
            }


            for (TimeTask task : timeTasks) {
                limiter.acquire();
                addTask(task);
                limiter.release();
            }
            scheduleLock.lock();
            schedule.start();

            logger.info("初始化定时任务success...");


        } catch (Exception e) {
            logger.error("初始化定时任务失败", e);

        } finally {
            if (scheduleLock.isLocked()) {
                scheduleLock.unlock();
            }
        }


    }


    /**
     * 添加定时任务
     *
     * @param task
     */
    private void addTask(TimeTask task) {

        logger.info("开始添加定时任务{}...", task);
        try {
            scheduleLock.lock();
            //检测是否已经存在
            if (schedule.checkExists(new JobKey(task.getTimeTaskName(), JOB_DETAIL_GROUP))) {
                logger.info("已经添加过定时任务{}...", task);
                return;
            }
            initSchedule(task.getTimeTaskName());

            jobDetailLock.lock();
            jobDetail.getJobDataMap().put(task.getTimeTaskName(), task);

            String cronExpress = task.getExecuteTime();
            CronScheduleBuilder cronBuild = CronScheduleBuilder.cronSchedule(cronExpress);
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(task.getTimeTaskName(), JOB_TRIGGER_GROUP).withSchedule(cronBuild).build();


            schedule.scheduleJob(jobDetail, trigger);

            //判断是否需要重启
            if (!schedule.isStarted()) {
                logger.info("启动定时任务success");
                schedule.start();
            }


            logger.info("添加定时任务{}success...", task);
        } catch (Exception e) {
            logger.error("添加定时任务{},失败", task, e);

        } finally {
            if (jobDetailLock.isLocked()) {
                jobDetailLock.unlock();
            }
            if (scheduleLock.isLocked()) {
                scheduleLock.unlock();
            }
        }


    }


    /**
     * 删除定时任务
     *
     * @param task
     */
    private void deleteTask(TimeTask task) {

        logger.info("开始删除定时任务{}...", task);
        try {

            JobKey jobKey = new JobKey(task.getTimeTaskName(), JOB_DETAIL_GROUP);
            scheduleLock.lock();
            if (schedule.checkExists(jobKey)) {

                schedule.pauseJob(jobKey);
                schedule.unscheduleJob(new TriggerKey(task.getTimeTaskName(), JOB_TRIGGER_GROUP));
                schedule.deleteJob(jobKey);

                logger.info("删除定时任务{}success...", task);
            } else {
                logger.info("没有查询到定时任务{}...", task);
            }


        } catch (Exception e) {
            logger.error("删除定时任务{},失败", task, e);
        } finally {
            if (scheduleLock.isLocked()) {
                scheduleLock.unlock();
            }
        }


    }

    /**
     * 更新定时任务
     *
     * @param task
     */
    private void updateTask(TimeTask task) {

        logger.info("开始更新定时任务{}...", task);
        try {
            TriggerKey triggerKey = new TriggerKey(task.getTimeTaskName(), JOB_TRIGGER_GROUP);
            scheduleLock.lock();

            if (schedule.checkExists(triggerKey)) {
                String cronExpress = task.getExecuteTime();
                CronScheduleBuilder cron = CronScheduleBuilder.cronSchedule(cronExpress);
                Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cron).build();
                trigger.getJobDataMap().put(task.getTimeTaskName(), task);

                schedule.rescheduleJob(triggerKey, trigger);


                logger.info("更新定时任务{}success...", task);
            } else {
                logger.info("没有查询到定时任务{}...", task);
            }


        } catch (Exception e) {
            logger.error("更新定时任务{},失败", task, e);
        } finally {
            if (scheduleLock.isLocked()) {
                scheduleLock.unlock();
            }
        }


    }

    /**
     * 立即执行定时任务
     *
     * @param task
     */
    private void startNowTask(TimeTask task) {

        logger.info("开始立即执行定时任务{}...", task);
        try {
            TriggerKey triggerKey = new TriggerKey(task.getTimeTaskName(), JOB_TRIGGER_GROUP);
            String cronExpress = task.getExecuteTime();
            CronScheduleBuilder cron = CronScheduleBuilder.cronSchedule(cronExpress);

            //立即执行
            Trigger trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).startNow().build();

            trigger.getJobDataMap().put(task.getTimeTaskName(), task);

            scheduleLock.lock();
            if (schedule.checkExists(triggerKey)) {

                schedule.rescheduleJob(triggerKey, trigger);

                logger.info("更新已经存在的并开始立即执行定时任务{}success...", task);
            } else {
                initSchedule(task.getTimeTaskName());

                schedule.scheduleJob(jobDetail, trigger);

                logger.info("重新创建并开始立即执行定时任务{}success...", task);
            }

            //重新出发任务 恢复指定的cron
            trigger = TriggerBuilder.newTrigger().withIdentity(triggerKey).withSchedule(cron).build();
            trigger.getJobDataMap().put(task.getTimeTaskName(), task);

            schedule.rescheduleJob(triggerKey, trigger);


            //判断是否需要重启
            if (!schedule.isStarted()) {
                logger.info("立即启动定时任务success");
                schedule.start();
            }

        } catch (Exception e) {
            logger.error("开始立即执行定时任务{},失败", task, e);
        } finally {
            if (scheduleLock.isLocked()) {
                scheduleLock.unlock();
            }
        }


    }


    /**
     * 执行定时任务
     *
     * @param tasks 定时任务
     */
    public void execute(TimeTask... tasks) {
        if (tasks == null || tasks.length == 0) {
            return;
        }

        try {
            for (TimeTask timeTask : tasks) {
                logger.info("开始处理定时任务{}", timeTask);
                switch (timeTask.getScheduleOperaEnum()) {
                    case ADD_TASK:
                        if (timeTask.wasEmpty()) {
                            logger.warn("添加的timerTask={}参数不对", timeTask);
                            continue;
                        }
                        addTask(timeTask);
                        break;
                    case UPDATE_TASK:
                        if (timeTask.wasEmpty()) {
                            logger.warn("更新的timerTask={}参数不对", timeTask);
                            continue;
                        }
                        updateTask(timeTask);
                        break;
                    case DELETE_TASK:
                        deleteTask(timeTask);
                        break;
                    case START_NOW_TASK:
                        if (timeTask.wasEmpty()) {
                            logger.warn("立即执行的timerTask={}参数不对", timeTask);
                            continue;
                        }
                        startNowTask(timeTask);
                        break;
                    default:
                        logger.warn("所传处理定时任务{},不符合规范", timeTask);
                        return;

                }

                logger.info("结束处理定时任务{}", timeTask);
            }


        } catch (Exception e) {
            logger.error("处理定时任务{}失败", Arrays.asList(tasks), e);
        }


    }

    public synchronized void destroy() {

        try {

            if (schedule != null && !schedule.isShutdown()) {
                schedule.shutdown();
                logger.info("销毁定时任务成功");
            }

        } catch (SchedulerException e) {
            logger.error("销毁定时任务失败", e);
        }


    }


}
