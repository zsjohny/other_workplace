package com.finace.miscroservice.borrow.config;

import com.finace.miscroservice.commons.config.RedisTemplateConfig;
import com.finace.miscroservice.commons.handler.impl.RedisDistributeLockHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@Configuration
public class RedisConfig extends RedisTemplateConfig {



    @Bean(name = "userHashRedisTemplate")
    public HashOperations<String, String, String> createZuulHashRedisTemplate() {
        return createTemplateCache(OperateEnum.HASH).opsForHash();
    }

    @Bean(name = "userStrHashRedisTemplate")
    public ValueOperations<String, String> createStrHashRedisTemplate() {
        return createTemplateCache(OperateEnum.STR).opsForValue();
    }


    @Bean(name = "strHashRedisTemplate")
    public RedisTemplate strHashRedisTemplate() {
        return createTemplateCache(OperateEnum.STR);
    }



}
