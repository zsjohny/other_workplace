package com.finace.miscroservice.task_scheduling.entity;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 定时任务延迟队列实体类
 */
public class TimerSchedulerDelayTask implements Serializable {

    private static final long serialVersionUID = 4143624702988486197L;
    /**
     * 通道名称
     */
    private String channelName;


    /**
     * 发送的内容
     */
    private String sendContent;

    /**
     * 任务Id
     */
    private String uuid;


    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getSendContent() {
        return sendContent;
    }

    public void setSendContent(String sendContent) {
        this.sendContent = sendContent;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public boolean wasEmpty() {
        if (StringUtils.isAnyEmpty(sendContent, channelName, uuid)) {
            return true;
        }

        return false;
    }
}
