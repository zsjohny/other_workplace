package com.finace.miscroservice.task_scheduling.task;


import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.distribute_task.timerTask.TimeTaskJob;
import com.finace.miscroservice.task_scheduling.entity.TimerTransfer;
import org.springframework.stereotype.Component;

import static com.finace.miscroservice.task_scheduling.task.TimerSchedulerTask.sendTimerScheduler;

@Component
public class TimerSchedulerJob extends TimeTaskJob {


    private Log logger = Log.getInstance(TimerSchedulerJob.class);


    @Override
    public void job(String params) {

        logger.info("开始执行定时任务 ,任务参数={}", params);
        TimerTransfer timerTransfer = JSONObject.parseObject(params, TimerTransfer.class);

        //检测参数定时任务参数
        if (timerTransfer == null || timerTransfer.wasEmpty()) {
            logger.warn("定时任务参数 uuid={} 为空 {}", timerTransfer == null ? null : timerTransfer.getUuid(), timerTransfer);
            return;
        }

        //执行发送定时任务消息
        sendTimerScheduler(timerTransfer.getSendType(), timerTransfer.getMsg());

        logger.info("结束执行定时任务 ,任务参数={}", params);
    }
}
