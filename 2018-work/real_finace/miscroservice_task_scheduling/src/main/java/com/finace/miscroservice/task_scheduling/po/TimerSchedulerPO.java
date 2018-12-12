package com.finace.miscroservice.task_scheduling.po;

import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;

/**
 * 定时器调度的PO
 */
public class TimerSchedulerPO {

    /**
     * 定时任务Id
     */
    private Integer id;

    /**
     * 定时任务的uuid
     */

    private String uuid;

    /**
     * 定时任务名称
     */
    private String timerSchedulerName;

    /**
     * 定时任务 时间表达式(eg: * * * * * ?)
     */
    private String timerSchedulerCron;


    /**
     * 定时任务参数(json格式)
     */
    private String timerSchedulerParam;

    /**
     * 定时任务的类型 更多参考timerSchedulerTypeEnum枚举
     */

    private Integer timerSchedulerType;


    /**
     * 创建时间
     */
    private Timestamp createTime;
    /**
     * 最后修改时间
     */
    private Timestamp updateTime;
    /**
     * 是否删除 0 false 1 true
     */
    private Integer deleted;


    /**
     * 检查是否为空
     *
     * @return true 为空 false 为全不空
     */
    public boolean wasEmpty() {
        if (StringUtils.isAnyEmpty(uuid, timerSchedulerName, timerSchedulerParam)) {
            return true;
        }
        return false;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getTimerSchedulerName() {
        return timerSchedulerName;
    }

    public void setTimerSchedulerName(String timerSchedulerName) {
        this.timerSchedulerName = timerSchedulerName;
    }

    public String getTimerSchedulerCron() {
        return timerSchedulerCron;
    }

    public void setTimerSchedulerCron(String timerSchedulerCron) {
        this.timerSchedulerCron = timerSchedulerCron;
    }

    public String getTimerSchedulerParam() {
        return timerSchedulerParam;
    }

    public void setTimerSchedulerParam(String timerSchedulerParam) {
        this.timerSchedulerParam = timerSchedulerParam;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Timestamp getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getTimerSchedulerType() {
        return timerSchedulerType;
    }

    public void setTimerSchedulerType(Integer timerSchedulerType) {
        this.timerSchedulerType = timerSchedulerType;
    }


    @Override
    public String toString() {
        return "TimerSchedulerPO{" +
                "id=" + id +
                ", uuid='" + uuid + '\'' +
                ", timerSchedulerName='" + timerSchedulerName + '\'' +
                ", timerSchedulerCron='" + timerSchedulerCron + '\'' +
                ", timerSchedulerParam='" + timerSchedulerParam + '\'' +
                ", timerSchedulerType=" + timerSchedulerType +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", deleted=" + deleted +
                '}';
    }
}
