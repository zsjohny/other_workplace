package com.finace.miscroservice.getway.config;

import com.finace.miscroservice.commons.config.RedisTemplateConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;

@Configuration
public class RedisConfig extends RedisTemplateConfig {


    @Bean(name = "zuulHashRedisTemplate")
    public HashOperations<String, String, String> createZuulHashRedisTemplate() {
        return createTemplateCache(OperateEnum.HASH).opsForHash();

    }

    @Bean(name = "uidHashRedisTemplate")
    public HashOperations<String, String, Integer> createUidHashRedisTemplate() {
        return createTemplateCache(OperateEnum.HASH).opsForHash();

    }

    @Bean(name = "userLoadRedisTemplate")
    public HashOperations<String, String, String> createUserLoadRedisTemplate() {
        return createTemplateCache(OperateEnum.HASH).opsForHash();

    }
}
