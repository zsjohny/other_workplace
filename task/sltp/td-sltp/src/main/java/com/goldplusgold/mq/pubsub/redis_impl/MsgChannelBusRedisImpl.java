package com.goldplusgold.mq.pubsub.redis_impl;

import com.goldplusgold.mq.pubsub.MQPublisher;
import com.goldplusgold.mq.pubsub.MQSubscriber;
import com.goldplusgold.mq.pubsub.MsgChannelBus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.stereotype.Repository;

/**
 * 消息订阅服务Redis实现, 用于应用间通信
 * Created by Administrator on 2017/5/11.
 */
@Repository("msgChannelBusRedisImpl")
public class MsgChannelBusRedisImpl implements MsgChannelBus {
    @Autowired
    @Qualifier("listenerContainerForMsgChannelBus")
    private RedisMessageListenerContainer listenerContainer;

    @Autowired
    @Qualifier("redisTemplateForMsgChannelBus")
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public boolean init(String[] args) {
        throw new RuntimeException("Nonsupport operation");
    }

    @Override
    public MQPublisher registerPublisher(String channel) {
        return new MQPublisherRedisImpl(channel, redisTemplate);
    }

    @Override
    public boolean registerSubscriber(MQSubscriber subscriber, String channel) {
        if (subscriber == null || channel == null) return false;
        MessageListenerAdapter adapter = new MessageListenerAdapter((MsgDelegate) msg -> subscriber.onMsg(msg));
        adapter.setSerializer(new GenericJackson2JsonRedisSerializer());
        adapter.afterPropertiesSet();
        ChannelTopic channelTopic = new ChannelTopic(channel);
        listenerContainer.addMessageListener(adapter, channelTopic);
        return true;
    }

    private interface MsgDelegate{
        void handleMessage(Object msg);
    }
}
