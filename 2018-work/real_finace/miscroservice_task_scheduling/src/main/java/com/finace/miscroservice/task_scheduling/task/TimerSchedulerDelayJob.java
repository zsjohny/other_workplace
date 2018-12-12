package com.finace.miscroservice.task_scheduling.task;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.commons.config.MqListenerConvert;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.ApplicationContextUtil;
import com.finace.miscroservice.task_scheduling.dao.TimerSchedulerDao;
import com.finace.miscroservice.task_scheduling.entity.TimerSchedulerDelayTask;

import static com.finace.miscroservice.task_scheduling.task.TimerSchedulerTask.sendTimerScheduler;


/**
 * 延迟定时任务工作类
 */
public class TimerSchedulerDelayJob extends MqListenerConvert {

    private Log logger = Log.getInstance(TimerSchedulerDelayJob.class);

    private TimerSchedulerDao timerSchedulerDao;

    @Override
    protected void transferTo(String transferData) {

        logger.info("开始处理延迟任务参数={}", transferData);
        try {
            TimerSchedulerDelayTask task = JSONObject.parseObject(transferData, TimerSchedulerDelayTask.class);

            if (task.wasEmpty()) {
                logger.warn("处理延迟任务参数={} 参数为空", transferData);
                return;
            }

            //执行发送定时任务消息
            sendTimerScheduler(task.getChannelName(), task.getSendContent());

            //删除定时任务
            if (timerSchedulerDao == null) {
                this.timerSchedulerDao = ApplicationContextUtil.getBean(TimerSchedulerDao.class);
            }

            timerSchedulerDao.removeTimerSchedulerByUUId(task.getUuid());

            logger.info("成功处理延迟任务={}", transferData);


        } catch (Exception e) {
            logger.error("处理延迟任务参数={} 失败", transferData, e);
        }

    }
}
