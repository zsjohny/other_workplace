package com.goldplusgold.mq.pubsub.rabbitmq_impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldplusgold.mq.msgs.DynamicQuotationBOWrapper;
import com.goldplusgold.mq.msgs.TestMsg;
import com.goldplusgold.mq.msgs.Trade2Sltp;
import com.goldplusgold.mq.msgs.UserOffset;
import com.goldplusgold.mq.pubsub.MQPublisher;
import com.goldplusgold.mq.pubsub.MQSubscriber;
import com.goldplusgold.mq.pubsub.MsgChannelBus;
import com.goldplusgold.mq.pubsub.PubSubChannels;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 消息订阅服务RabbitMq实现, 用于应用间通信
 * Created by Administrator on 2017/5/17.
 */
@Component("msgChannelBusRabbitMqImpl")
public class MsgChannelBusRabbitMqImpl implements MsgChannelBus{
    private Logger logger = LoggerFactory.getLogger(MsgChannelBusRabbitMqImpl.class);

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    @Qualifier("amqpTemplateForMsgChannelBus")
    private AmqpTemplate amqpTemplate;

    private Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();

    private ObjectMapper objectMapper = new ObjectMapper();

    private Map<String, Class> channelClassMappers = new HashMap<>();

    public MsgChannelBusRabbitMqImpl() {
        channelClassMappers.put(PubSubChannels.CH_FOR_TEST, TestMsg.class);
        channelClassMappers.put(PubSubChannels.CH_QUOTATION, DynamicQuotationBOWrapper.class);
        channelClassMappers.put(PubSubChannels.CH_QUOTATION_TEST, DynamicQuotationBOWrapper.class);
    }

    @Override
    public boolean init(String[] args) {
        throw new RuntimeException("Nonsupport operation");
    }

    @Override
    public MQPublisher registerPublisher(String channel) {
        FanoutExchange fanoutExchange = new FanoutExchange(channel);
        amqpAdmin.declareExchange(fanoutExchange);
        MQPublisher mqPublisher = new MQPublisherRabbitMqImpl(channel, amqpTemplate);
        return mqPublisher;
    }

    @Override
    public boolean registerSubscriber(MQSubscriber subscriber, String channel) {
        Class targetlass = channelClassMappers.get(channel);
        if (targetlass==null) throw new RuntimeException("频道["+channel+"]的目标类型未配置!");

        FanoutExchange fanoutExchange = new FanoutExchange(channel);
        amqpAdmin.declareExchange(fanoutExchange);
        Queue queue = new Queue(channel + UUID.randomUUID().toString());
        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareBinding(BindingBuilder.bind(queue).to(fanoutExchange));

        MessageListenerAdapter adapter = new MessageListenerAdapter(new MsgDelegate() {
            @Override
            public void handleMessage(Object msg) {
                //打印原始消息
//                Message message = (Message) msg;
//                System.out.println(new String(message.getBody()));
                try {
                    Message message = (Message) msg;
                    Object obj = objectMapper.readValue(new String(message.getBody()), targetlass);
                    subscriber.onMsg(obj);
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.warn(e.getMessage());
                }
            }
        });
        adapter.setMessageConverter(null);

        SimpleMessageListenerContainer listenerContainer = new SimpleMessageListenerContainer(connectionFactory);
        listenerContainer.setQueues(queue);
        listenerContainer.setMessageListener(adapter);
        listenerContainer.afterPropertiesSet();
        listenerContainer.start();
        return true;
    }

    private interface MsgDelegate{
        void handleMessage(Object msg);
    }

}
