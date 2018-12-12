package com.finace.miscroservice.task_scheduling.entity;

import org.apache.commons.lang3.StringUtils;

import java.beans.Transient;
import java.io.Serializable;

/**
 * 定时任务的接受类
 */
public class TimerTransfer implements Serializable {
    private static final long serialVersionUID = -7615812218479904642L;
    /**
     * 发送类型
     */
    private String sendType;


    /**
     * 发送内容
     */
    private String msg;


    /**
     * 定时任务的uuid
     */
    private String uuid;


    public String getSendType() {
        return sendType;
    }

    public void setSendType(String sendType) {
        this.sendType = sendType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }


    @Transient
    public boolean wasEmpty() {
        if (StringUtils.isAnyEmpty(sendType, msg, uuid)) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "TimerTransfer{" +
                "sendType='" + sendType + '\'' +
                ", msg='" + msg + '\'' +
                ", uuid='" + uuid + '\'' +
                '}';
    }
}

