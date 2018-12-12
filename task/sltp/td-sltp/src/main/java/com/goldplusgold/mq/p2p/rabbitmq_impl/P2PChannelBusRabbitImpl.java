package com.goldplusgold.mq.p2p.rabbitmq_impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.goldplusgold.mq.msgs.TestMsg;
import com.goldplusgold.mq.msgs.Trade2Sltp;
import com.goldplusgold.mq.msgs.UserOffset;
import com.goldplusgold.mq.p2p.P2PChannelBus;
import com.goldplusgold.mq.p2p.P2PChannels;
import com.goldplusgold.mq.p2p.P2PPublisher;
import com.goldplusgold.mq.p2p.P2PSubscriber;
import com.goldplusgold.mq.pubsub.PubSubChannels;
import com.goldplusgold.mq.pubsub.rabbitmq_impl.MsgChannelBusRabbitMqImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.channels.Channels;
import java.util.HashMap;
import java.util.Map;

/**
 * P2P消息服务RabbitMq实现, 用于应用间通信
 * Created by Administrator on 2017/5/18.
 */
@Component("p2PChannelBusRabbitImpl")
public class P2PChannelBusRabbitImpl implements P2PChannelBus {
    private Logger logger = LoggerFactory.getLogger(P2PChannelBusRabbitImpl.class);

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    @Qualifier("amqpTemplateForP2PChannelBus")
    private AmqpTemplate amqpTemplate;

    private Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();

    private ObjectMapper objectMapper = new ObjectMapper();

    private Map<String, Class> channelClassMappers = new HashMap<>();

    public P2PChannelBusRabbitImpl() {
        channelClassMappers.put(P2PChannels.P2P_CH_FOR_TEST, TestMsg.class);
        channelClassMappers.put(P2PChannels.CH_TRADE_NOTIFY, Trade2Sltp.class);
        channelClassMappers.put(P2PChannels.CH_USER_OFFSET, UserOffset.class);
        channelClassMappers.put(P2PChannels.CH_USER_OFFSET_TEST, UserOffset.class);
    }

    @Override
    public boolean init(String[] args) {
        throw new RuntimeException("Nonsupport operation");
    }

    @Override
    public P2PPublisher registerPublisher(String channel) {
        return new P2PPublisherRabbitMqImpl(channel, amqpTemplate);
    }

    @Override
    public boolean registerSubscriber(P2PSubscriber subscriber, String channel) {
        Class targetlass = channelClassMappers.get(channel);
        if (targetlass==null) throw new RuntimeException("频道["+channel+"]的目标类型未配置!");

        Queue queue = new Queue(channel);
        amqpAdmin.declareQueue(queue);

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
