package com.finace.miscroservice.config;

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


}
