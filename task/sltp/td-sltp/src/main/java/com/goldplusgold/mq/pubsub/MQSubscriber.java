package com.goldplusgold.mq.pubsub;

/**
 * 频道消息订阅者公共接口
 * Created by Administrator on 2017/5/10.
 */
public interface MQSubscriber {
    /**
     * 消费消息
     * @param msg 消息
     */
    void onMsg(Object msg);
}
