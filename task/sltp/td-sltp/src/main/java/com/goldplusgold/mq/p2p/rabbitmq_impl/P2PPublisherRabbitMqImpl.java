package com.goldplusgold.mq.p2p.rabbitmq_impl;

import com.goldplusgold.mq.p2p.P2PPublisher;
import org.springframework.amqp.core.AmqpTemplate;

/**
 * P2P消息发布者RabbitMq实现, 用于应用间通信
 * Created by Administrator on 2017/5/18.
 */
public class P2PPublisherRabbitMqImpl implements P2PPublisher {
    private final String channelName;
    private final AmqpTemplate amqpTemplate;

    public P2PPublisherRabbitMqImpl(String channelName, AmqpTemplate amqpTemplate) {
        this.channelName = channelName;
        this.amqpTemplate = amqpTemplate;
    }

    @Override
    public boolean publish(Object msg) {
        if (msg != null){
            amqpTemplate.convertAndSend(null, channelName, msg);
            return true;
        }else {
            return false;
        }
    }
}
