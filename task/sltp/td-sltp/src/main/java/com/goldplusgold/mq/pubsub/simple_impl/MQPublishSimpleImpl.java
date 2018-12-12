package com.goldplusgold.mq.pubsub.simple_impl;

import com.goldplusgold.mq.pubsub.MQPublisher;

import java.util.concurrent.BlockingQueue;

/**
 * 频道消息发布者简单实现, 供应用内使用
 * Created by Administrator on 2017/5/11.
 */
public class MQPublishSimpleImpl implements MQPublisher {
    private final BlockingQueue<Object> ch;

    MQPublishSimpleImpl(BlockingQueue<Object> ch) {
        this.ch = ch;
    }

    @Override
    public boolean publish(Object msg) {
        return ch.offer(msg);
    }

}
