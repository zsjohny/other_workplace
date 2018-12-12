package com.goldplusgold.mq.pubsub.redis_impl;

import com.goldplusgold.mq.pubsub.MQPublisher;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 频道消息发布者Redis实现, 用于应用间通信
 * Created by Administrator on 2017/5/12.
 */
public class MQPublisherRedisImpl implements MQPublisher {
    private final String channel;
    private final RedisTemplate<String, Object> redisTemplate;

    MQPublisherRedisImpl(String channel, RedisTemplate<String, Object> redisTemplate) {
        this.channel = channel;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public boolean publish(Object msg) {
        redisTemplate.convertAndSend(channel, msg);
        return true;
    }
}
