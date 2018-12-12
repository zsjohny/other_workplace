package com.goldplusgold.td.sltp.core.operate.scheduler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 定时任务总控制中心
 * Created by Ness on 2017/5/16.
 */
@EnableScheduling
public abstract class SchedulerBus {


    /**
     * 定时任务
     */
    protected abstract void scheduler();

    private Logger logger = LoggerFactory.getLogger(SchedulerBus.class);

    /**
     * 执行任务
     */
    protected void init() {
        logger.info("开始执行定时任务");
        task();
        logger.info("结束执行定时任务");
    }

    /**
     * 具体任务
     */
    protected abstract void task();


}
