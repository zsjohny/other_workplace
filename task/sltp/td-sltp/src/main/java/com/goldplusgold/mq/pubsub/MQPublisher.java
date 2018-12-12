package com.goldplusgold.mq.pubsub;

/**
 * 频道消息发布者公共接口
 * Created by Administrator on 2017/5/10.
 */
public interface MQPublisher {
    /**
     * 发布消息
     * @param msg 消息
     * @return 是否发布成功
     */
    boolean publish(Object msg);
}
