package com.finace.miscroservice.commons.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis的配置
 */
public class RedisTemplateConfig {


    protected enum OperateEnum {
        HASH, SCRIPT, STR, DEFAULT
    }

    @Autowired
    private JedisConnectionFactory factory;

    /**
     * 创建 redisTemplate的操作类
     *
     * @param operateEnum 创建类型
     * @return
     */
    protected RedisTemplate createTemplateCache(OperateEnum operateEnum) {
        RedisTemplate template = new RedisTemplate<>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(new StringRedisSerializer());
        switch (operateEnum) {
            case HASH:
                template.setHashKeySerializer(new StringRedisSerializer());
                template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
                break;
            case SCRIPT:
                template.setDefaultSerializer(new StringRedisSerializer());
                break;
            case STR:
                template.setValueSerializer(new JdkSerializationRedisSerializer());

                break;
            default:

                template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        }

        template.afterPropertiesSet();
        return template;
    }


}
