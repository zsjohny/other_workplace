package com.finace.miscroservice.commons.entity;

import java.io.Serializable;

/**
 * 定时调度任务实体类
 */
public class TimerScheduler implements Serializable {
    private static final long serialVersionUID = -6700613681953847129L;
    /**
     * 定时任务 时间表达式 格式(* * * * * * ?)
     */
    private String cron;
    /**
     * 定时任务的参数
     */
    private String params;

    /**
     * 定时任务的名称
     */
    private String name;


    /**
     * 定时任务类型 更多参考定时任务TimerSchedulerTypeEnum枚举
     */
    private Integer type;


    public String getCron() {
        return cron;
    }

    public void setCron(String cron) {
        this.cron = cron;
    }

    public String getParams() {
        return params;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }


    public boolean wasEmpty() {


        if (params == null || params.isEmpty()) {
            return true;
        }

        if (type == null) {
            return true;
        }
        if (name == null || name.isEmpty()) {
            return true;
        }

        return false;

    }

}
