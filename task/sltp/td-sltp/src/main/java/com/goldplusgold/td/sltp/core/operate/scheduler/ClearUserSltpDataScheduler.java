package com.goldplusgold.td.sltp.core.operate.scheduler;

import com.goldplusgold.td.sltp.core.operate.component.RedisHashOperateComponent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * 定时清理行情类
 * Created by Ness on 2017/5/16.
 */
@Configuration
public class ClearUserSltpDataScheduler extends SchedulerBus {

    @Autowired
    private RedisHashOperateComponent redisHashOperateComponent;

    @Scheduled(cron = "0 0 16 * * ?")
    @Override
    public void scheduler() {
        super.init();
    }


    @Override
    public void task() {

        redisHashOperateComponent.clear();

    }
}
