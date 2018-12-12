package com.goldplusgold.mq.pubsub.redis_impl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 消息订阅服务Redis实现-配置信息
 * Created by Administrator on 2017/5/11.
 */
@Configuration
public class MsgChannelBusRedisImplConf {

    @Bean(name = "listenerContainerForMsgChannelBus")
    public RedisMessageListenerContainer createRedisMessageListenerContainer(JedisConnectionFactory jedisConnectionFactory){
        RedisMessageListenerContainer listenerContainer = new RedisMessageListenerContainer();
        listenerContainer.setConnectionFactory(jedisConnectionFactory);
        listenerContainer.afterPropertiesSet();
        return listenerContainer;
    }

    @Bean(name = "redisTemplateForMsgChannelBus")
    public RedisTemplate<String, Object> createRedisTemplate(JedisConnectionFactory jedisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

}
