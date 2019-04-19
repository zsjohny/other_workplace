package com.e_commerce.miscroservice.publicaccount.config;

import com.e_commerce.miscroservice.commons.config.colligate.RedisTemplateConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

//
//@Configuration
//public class RedisConfig extends RedisTemplateConfig {
//
//
//    @Bean(name = "strRedisTemplate")
//    public RedisTemplate createUserHashRedisTemplate() {
//        return createTemplateCache (OperateEnum.STR);
//    }
//
//}
