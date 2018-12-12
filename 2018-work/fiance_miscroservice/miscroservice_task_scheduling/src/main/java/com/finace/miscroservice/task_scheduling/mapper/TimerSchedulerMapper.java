package com.finace.miscroservice.task_scheduling.mapper;

import com.finace.miscroservice.task_scheduling.po.TimerSchedulerPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 任务调度的 mapper
 */
@Mapper
public interface TimerSchedulerMapper {
    /**
     * 保存定时任务
     *
     * @param timerSchedulerPO 定时任务实体类
     * @return
     */
    int saveTimerScheduler(TimerSchedulerPO timerSchedulerPO);

    /**
     * 查询所有未过期的定时任务
     *
     * @return
     */
    List<TimerSchedulerPO> findTimerSchedulerAllByNotExpire();

    /**
     * 根据Id删除 timerScheduler
     *
     * @param uuid 定时任务的Id
     * @return
     */
    Boolean removeTimerSchedulerByUUId(@Param("uuid") String uuid);


}
