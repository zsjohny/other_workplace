package com.goldplusgold.mq.p2p;

/**
 * P2P消息订阅者公共接口
 * Created by Administrator on 2017/5/18.
 */
public interface P2PSubscriber {
    /**
     * 消费消息
     * @param msg 消息
     */
    void onMsg(Object msg);
}
