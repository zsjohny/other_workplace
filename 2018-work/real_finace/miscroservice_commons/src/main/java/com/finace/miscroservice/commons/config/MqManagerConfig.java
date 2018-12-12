package com.finace.miscroservice.commons.config;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * Mq的管理配置中心
 */
public class MqManagerConfig {

    /**
     * 域名
     */
    private String host;
    /**
     * 端口
     */
    private int port;

    /**
     * 用户名称
     */
    private String userName;
    /**
     * 用户密码
     */
    private String userPass;
    /**
     * 发送消息的集合管理  格式  消息名称:消息的类型(fanout,direct):接受的监听类的名称:是否是随机生成队列名称(最后一项可选,并且只有在队列为fanout才生效 true是 false不是) eg msg:fanout:demoListener:true(这项可选)
     */
    private List<String> channels;

    /**
     * 是否打开发送 true打开 false关闭　默认关闭
     */
    private Boolean openSend;


    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public List<String> getChannels() {
        return channels;
    }

    public void setChannels(List<String> channels) {
        this.channels = channels;
    }

    public Boolean getOpenSend() {
        return openSend;
    }

    public void setOpenSend(Boolean openSend) {
        this.openSend = openSend;
    }

    public boolean isEmpty() {
        if (StringUtils.isAnyEmpty(host, userName, userPass) || port <= 0) {
            return true;
        }
        return false;
    }

}
