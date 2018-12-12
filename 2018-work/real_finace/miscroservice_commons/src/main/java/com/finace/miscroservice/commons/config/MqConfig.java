package com.finace.miscroservice.commons.config;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;

import java.util.Map;

/**
 * mq配置
 */
public class MqConfig {
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
     * 队列名称
     */
    private String queueName;
    /**
     * 交换器名称
     */
    private String exchangeName;
    /**
     * 路由名称
     */
    private String routingKeyName;

    /**
     * 是否随机生成队列名称
     */
    private Boolean isRandomQueueName;

    /**
     * mq的监听转化类
     */
    private MqListenerConvert mqListenerConvert;


    /**
     * 注册的操作工厂
     */
    private DefaultListableBeanFactory factory;

    /**
     * 配置mq常用的一些参数集合
     */
    private Map<String, Object> mqArguments;

    /**
     * ttl队列的延迟时间(毫秒结尾)
     */
    private Long TTL_DELAY_TIME;

    /**
     * ttl队列的队列名称
     */
    private String TTL_QUEUE_NAME;
    /**
     * ttl队列的交换器名称
     */
    private String TTL_EXCHANGE_NAME;


    /**
     * ttl队列的路由名称
     *
     * @return
     */
    private String TTL_ROUTING_KEY_NAME;


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

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getRoutingKeyName() {
        return routingKeyName;
    }

    public void setRoutingKeyName(String routingKeyName) {
        this.routingKeyName = routingKeyName;
    }


    public MqListenerConvert getMqListenerConvert() {
        return mqListenerConvert;
    }

    public void setMqListenerConvert(MqListenerConvert mqListenerConvert) {
        this.mqListenerConvert = mqListenerConvert;
    }

    public DefaultListableBeanFactory getFactory() {
        return factory;
    }

    public void setFactory(DefaultListableBeanFactory factory) {
        this.factory = factory;
    }


    public Map<String, Object> getMqArguments() {
        return mqArguments;
    }

    public void setMqArguments(Map<String, Object> mqArguments) {
        this.mqArguments = mqArguments;
    }

    public Long getTTL_DELAY_TIME() {
        return TTL_DELAY_TIME;
    }

    public void setTTL_DELAY_TIME(Long TTL_DELAY_TIME) {
        this.TTL_DELAY_TIME = TTL_DELAY_TIME;
    }

    public String getTTL_EXCHANGE_NAME() {
        return TTL_EXCHANGE_NAME;
    }

    public void setTTL_EXCHANGE_NAME(String TTL_EXCHANGE_NAME) {
        this.TTL_EXCHANGE_NAME = TTL_EXCHANGE_NAME;
    }

    public String getTTL_ROUTING_KEY_NAME() {
        return TTL_ROUTING_KEY_NAME;
    }

    public void setTTL_ROUTING_KEY_NAME(String TTL_ROUTING_KEY_NAME) {
        this.TTL_ROUTING_KEY_NAME = TTL_ROUTING_KEY_NAME;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Boolean getRandomQueueName() {
        return isRandomQueueName;
    }

    public void setRandomQueueName(Boolean randomQueueName) {
        isRandomQueueName = randomQueueName;
    }

    public String getTTL_QUEUE_NAME() {
        return TTL_QUEUE_NAME;
    }

    public void setTTL_QUEUE_NAME(String TTL_QUEUE_NAME) {
        this.TTL_QUEUE_NAME = TTL_QUEUE_NAME;
    }
}
