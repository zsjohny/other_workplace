package com.finace.miscroservice.official_website.config;

import com.finace.miscroservice.commons.config.RedisTemplateConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
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




}
