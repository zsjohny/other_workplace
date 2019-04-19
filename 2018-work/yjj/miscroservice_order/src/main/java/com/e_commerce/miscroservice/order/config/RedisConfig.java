package com.e_commerce.miscroservice.order.config;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.config.colligate.RedisTemplateConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ValueOperations;

//@Configuration
//public class RedisConfig extends RedisTemplateConfig {
//
//
//    @Bean(name = "userHashRedisTemplate")
//    public HashOperations<String, String, String> createUserHashRedisTemplate() {
//        return createTemplateCache(OperateEnum.HASH).opsForHash();
//    }
//
//    @Bean(name = "userStrHashRedisTemplate")
//    public ValueOperations<String, String> createStrHashRedisTemplate() {
//        return createTemplateCache(OperateEnum.STR).opsForValue();
//    }
//
//    @Bean(name = "userJsonObjHashRedisTemplate")
//    public ValueOperations<String, JSONObject> createJsonObjHashRedisTemplate() {
//        return createTemplateCache(OperateEnum.DEFAULT).opsForValue();
//    }
//
//}
