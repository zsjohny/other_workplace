package com.finace.miscroservice.task_scheduling.listener;

import com.alibaba.fastjson.JSONObject;
import com.finace.miscroservice.commons.config.MqListenerConvert;
import com.finace.miscroservice.commons.config.MqTemplate;
import com.finace.miscroservice.commons.entity.TimerScheduler;
import com.finace.miscroservice.commons.enums.TimerSchedulerTypeEnum;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.UUIdUtil;
import com.finace.miscroservice.task_scheduling.dao.TimerSchedulerDao;
import com.finace.miscroservice.task_scheduling.entity.TimerSchedulerDelayTask;
import com.finace.miscroservice.task_scheduling.po.TimerSchedulerPO;
import com.finace.miscroservice.task_scheduling.task.TimerSchedulerTask;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import static com.finace.miscroservice.task_scheduling.task.TimerSchedulerTask.DELAY_TASK_NAME_PREFIX;


/**
 * TimerTask 的mq监听类
 */
@Component
public class TimerSchedulerMqListener extends MqListenerConvert {

    private Log logger = Log.getInstance(TimerSchedulerMqListener.class);


    @Autowired
    private TimerSchedulerDao timerSchedulerDao;

    @Autowired
    private TimerSchedulerTask timerSchedulerTask;

    @Autowired
    @Lazy
    private MqTemplate mqTemplate;

    @Override
    protected void transferTo(String transferData) {

        try {
            logger.info("开始解析定时任务={}", transferData);

            TimerScheduler scheduler = JSONObject.parseObject(transferData, TimerScheduler.class);

            if (scheduler == null || scheduler.wasEmpty()) {
                logger.warn("定时任务={} 解析参数为空", transferData);
                return;
            }

            //复制属性
            TimerSchedulerPO timerSchedulerPO = copyParams(scheduler);

            //存储db
            if (!timerSchedulerDao.saveTimerScheduler(timerSchedulerPO)) {
                logger.warn("数据库已经存在相同的任务={} 无需再次创建 过滤", timerSchedulerPO.getTimerSchedulerParam());
                return;
            }

            //判断是延迟任务还是定时任务
            if (StringUtils.isEmpty(timerSchedulerPO.getTimerSchedulerCron())) {
                //延迟任务
                TimerSchedulerDelayTask schedulerDelayTask = new TimerSchedulerDelayTask();

                schedulerDelayTask.setChannelName(TimerSchedulerTypeEnum.num2Char(timerSchedulerPO.getTimerSchedulerType()));

                schedulerDelayTask.setSendContent(scheduler.getParams());

                schedulerDelayTask.setUuid(timerSchedulerPO.getUuid());

                //发送延迟队列 延迟队列的通道加DELAY_TASK_NAME_PREFIX即可
                mqTemplate.sendMsg(schedulerDelayTask.getChannelName() + DELAY_TASK_NAME_PREFIX, JSONObject.toJSONString(schedulerDelayTask));

            } else {
                //定时任务
                //实例化定时任务对象
                timerSchedulerTask.startJob(timerSchedulerPO);
            }


            logger.info("成功解析定时任务={}", transferData);

        } catch (Exception e) {
            logger.error("解析定时任务={} 出错", transferData, e);
        }

    }

    /**
     * 拷贝参数
     *
     * @param timerScheduler 被拷贝的定时任务调度器实体类
     * @return
     */
    private TimerSchedulerPO copyParams(TimerScheduler timerScheduler) {
        TimerSchedulerPO timerSchedulerPO = new TimerSchedulerPO();
        timerSchedulerPO.setTimerSchedulerParam(timerScheduler.getParams());
        timerSchedulerPO.setTimerSchedulerCron(timerScheduler.getCron());
        timerSchedulerPO.setTimerSchedulerType(timerScheduler.getType());
        timerSchedulerPO.setUuid(UUIdUtil.generateUuid());
        timerSchedulerPO.setTimerSchedulerName(timerScheduler.getName());

        return timerSchedulerPO;
    }


}
