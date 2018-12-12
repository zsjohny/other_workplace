package com.goldplusgold.mq.p2p;

/**
 * P2P消息公共接口
 * Created by Administrator on 2017/5/18.
 */
public interface P2PChannelBus {
    /**
     * 初始化P2P消息服务(可选)
     * @param args 初始化参数
     * @return 是否初始化成功
     */
    boolean init(String[] args);

    /**
     * 注册消息发布者
     * @param channel 消息频道
     * @return 成功返回实例, 失败返回null
     */
    P2PPublisher registerPublisher(String channel);

    /**
     * 注册消息消费者
     * @param subscriber 消息消费者
     * @param channel 消息频道
     * @return 是否注册成功
     */
    boolean registerSubscriber(P2PSubscriber subscriber, String channel);
}
