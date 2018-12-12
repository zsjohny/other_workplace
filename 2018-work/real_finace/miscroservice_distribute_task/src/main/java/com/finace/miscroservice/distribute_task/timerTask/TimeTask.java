package com.finace.miscroservice.distribute_task.timerTask;


import com.finace.miscroservice.distribute_task.util.CronUtil;

import java.io.Serializable;


/**
 * 定时任务的类
 */
public class TimeTask implements Serializable {

    private static final long serialVersionUID = -7615812218479904641L;
    /**
     * 任务名称
     */
    private String timeTaskName;

    /**
     * 执行时间
     */
    private String executeTime;
    /**
     * 任务参数
     */
    private String params;


    private ScheduleOperaEnum scheduleOperaEnum;


    public ScheduleOperaEnum getScheduleOperaEnum() {
        return scheduleOperaEnum;
    }

    public void setScheduleOperaEnum(ScheduleOperaEnum scheduleOperaEnum) {
        this.scheduleOperaEnum = scheduleOperaEnum;
    }


    public String getExecuteTime() {
        return executeTime;
    }

    public void setExecuteTime(String executeTime) {
        this.executeTime = executeTime;
    }


    public String getTimeTaskName() {
        return timeTaskName;
    }

    public void setTimeTaskName(String timeTaskName) {
        this.timeTaskName = timeTaskName;
    }


    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        if (params.contains("\"")) {
            params = params.replaceAll("\"", "'");
        }
        this.params = params;
    }

    public Boolean wasEmpty() {
        if (executeTime == null || executeTime.isEmpty()
                || timeTaskName == null || timeTaskName.isEmpty() || params == null || params.isEmpty()) {
            return true;
        }
        return !CronUtil.checkValidCron(executeTime);
    }


    @Override
    public String toString() {
        return "TimeTask{" +
                "timeTaskName='" + timeTaskName + '\'' +
                ", executeTime='" + executeTime + '\'' +
                ", params='" + params + '\'' +
                ", scheduleOperaEnum=" + scheduleOperaEnum +
                '}';
    }

}
