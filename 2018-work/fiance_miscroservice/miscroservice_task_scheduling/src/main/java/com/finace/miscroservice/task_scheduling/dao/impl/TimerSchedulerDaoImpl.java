package com.finace.miscroservice.task_scheduling.dao.impl;

import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.task_scheduling.dao.TimerSchedulerDao;
import com.finace.miscroservice.task_scheduling.mapper.TimerSchedulerMapper;
import com.finace.miscroservice.task_scheduling.po.TimerSchedulerPO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 任务调度的Dao实现类
 */
@Repository
public class TimerSchedulerDaoImpl implements TimerSchedulerDao {

    private Log logger = Log.getInstance(TimerSchedulerDaoImpl.class);


    @Autowired
    private TimerSchedulerMapper timerSchedulerMapper;


    @Override
    public Boolean saveTimerScheduler(TimerSchedulerPO timerSchedulerPO) {
        Boolean saveFlag = Boolean.FALSE;

        if (timerSchedulerPO.wasEmpty()) {
            logger.warn("存储定时任务所传参数有空 timerScheduler={}", timerSchedulerPO);
            return saveFlag;
        }

        if (timerSchedulerMapper.saveTimerScheduler(timerSchedulerPO) > 0) {
            logger.info("存储定时任务,定时任务UUID={} success", timerSchedulerPO.getUuid());
            saveFlag = Boolean.TRUE;
        }

        return saveFlag;
    }

    @Override
    public List<TimerSchedulerPO> findTimerSchedulerAllByNotExpire() {
        logger.info("查询所有没有过期的定时任务");
        return timerSchedulerMapper.findTimerSchedulerAllByNotExpire();
    }

    @Override
    public Boolean removeTimerSchedulerByUUId(String uuid) {
        Boolean removeFlag = Boolean.FALSE;
        if (StringUtils.isEmpty(uuid)) {
            logger.warn("定时任务删除所传Id为空");
            return removeFlag;

        }

        removeFlag = timerSchedulerMapper.removeTimerSchedulerByUUId(uuid);
        if (removeFlag) {
            logger.info("根据定时任务Id={} 删除定时任务 success", uuid);
        } else {
            logger.warn("根据定时任务Id={} 删除定时任务 fail", uuid);
        }


        return removeFlag;
    }
}
