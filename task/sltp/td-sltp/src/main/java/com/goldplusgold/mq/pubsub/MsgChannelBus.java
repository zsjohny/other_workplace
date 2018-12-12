package com.goldplusgold.mq.pubsub;

/**
 * 消息订阅服务公共接口
 * Created by Administrator on 2017/5/10.
 */
public interface MsgChannelBus {
    /**
     * 初始化消息订阅服务(可选)
     * @param args 初始化参数
     * @return 是否初始化成功
     */
    boolean init(String[] args);

    /**
     * 注册消息发布者
     * @param channel 消息频道
     * @return 成功返回实例, 失败返回null
     */
    MQPublisher registerPublisher(String channel);

    /**
     * 注册消息消费者
     * @param subscriber 消息消费者
     * @param channel 消息频道
     * @return 是否注册成功
     */
    boolean registerSubscriber(MQSubscriber subscriber, String channel);
}
