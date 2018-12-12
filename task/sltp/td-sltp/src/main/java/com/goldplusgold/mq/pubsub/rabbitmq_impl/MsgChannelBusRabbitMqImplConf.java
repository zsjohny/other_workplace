package com.goldplusgold.mq.pubsub.rabbitmq_impl;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 消息订阅服务RabbitMq实现, 配置类
 * Created by Administrator on 2017/5/17.
 */
@Configuration
public class MsgChannelBusRabbitMqImplConf {

    @Bean(name = "amqpTemplateForMsgChannelBus")
    public AmqpTemplate amqpTemplate(ConnectionFactory connectionFactory){
        RabbitTemplate amqpTemplate = new RabbitTemplate(connectionFactory);
        amqpTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        amqpTemplate.afterPropertiesSet();
        return amqpTemplate;
    }

}
