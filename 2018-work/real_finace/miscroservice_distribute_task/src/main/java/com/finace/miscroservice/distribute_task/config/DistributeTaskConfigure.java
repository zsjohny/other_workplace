package com.finace.miscroservice.distribute_task.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 分布式调度的配置类
 */
@ConfigurationProperties(prefix = "distribute.task")
public class DistributeTaskConfigure {

    /**
     * 是否开启分布式调度任务 true开始
     */
    private Boolean enabled;


    /**
     * zk的服务注册地址
     */
    private String serverList;

    /**
     * 继承timerTask的类的名称
     */
    private String timeTaskJobName;

    /**
     * 继承logUtil的类的名称
     */
    private String logUtilName;

    /**
     * 继承IpUtil的名称
     */
    private String ipUtilName;


    public String getServerList() {
        return serverList;
    }

    public void setServerList(String serverList) {
        this.serverList = serverList;
    }

    public String getTimeTaskJobName() {
        return timeTaskJobName;
    }

    public void setTimeTaskJobName(String timeTaskJobName) {
        this.timeTaskJobName = timeTaskJobName;
    }

    public String getLogUtilName() {
        return logUtilName;
    }

    public void setLogUtilName(String logUtilName) {
        this.logUtilName = logUtilName;
    }

    public String getIpUtilName() {
        return ipUtilName;
    }

    public void setIpUtilName(String ipUtilName) {
        this.ipUtilName = ipUtilName;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }
}
