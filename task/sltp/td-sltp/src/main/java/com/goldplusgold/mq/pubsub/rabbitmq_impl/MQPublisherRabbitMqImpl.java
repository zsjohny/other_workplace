package com.goldplusgold.mq.pubsub.rabbitmq_impl;

import com.goldplusgold.mq.pubsub.MQPublisher;
import org.springframework.amqp.core.AmqpTemplate;

/**
 * 频道消息发布者RabbitMq实现, 用于应用间通信
 * Created by Administrator on 2017/5/17.
 */
public class MQPublisherRabbitMqImpl implements MQPublisher{
    private final String channelName;
    private final AmqpTemplate amqpTemplate;

    MQPublisherRabbitMqImpl(String channelName, AmqpTemplate amqpTemplate) {
        this.channelName = channelName;
        this.amqpTemplate = amqpTemplate;
    }

    @Override
    public boolean publish(Object msg) {
        if (msg != null){
            amqpTemplate.convertAndSend(channelName, null, msg);
            return true;
        }else {
            return false;
        }
    }
}
