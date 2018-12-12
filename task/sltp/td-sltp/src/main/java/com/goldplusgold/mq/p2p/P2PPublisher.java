package com.goldplusgold.mq.p2p;

/**
 * P2P消息发布者公共接口
 * Created by Administrator on 2017/5/18.
 */
public interface P2PPublisher {
    /**
     * 发布消息
     * @param msg 消息
     * @return 是否发布成功
     */
    boolean publish(Object msg);
}
