package com.e_commerce.miscroservice.task.config;

import com.alibaba.fastjson.JSONObject;
import com.e_commerce.miscroservice.commons.config.colligate.RedisTemplateConfig;
import com.e_commerce.miscroservice.commons.entity.task.LiveData;
import com.e_commerce.miscroservice.commons.entity.task.MemberAudienceData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

@Configuration
public class TaskRedisConfig extends RedisTemplateConfig {


    @Bean(name = "userHashRedisTemplate")
    public HashOperations<String, String, String> createUserHashRedisTemplate() {
        return createTemplateCache(OperateEnum.HASH).opsForHash();
    }

    @Bean(name = "userStrHashRedisTemplate")
    public ValueOperations<String, String> createStrHashRedisTemplate() {
        return createTemplateCache(OperateEnum.STR).opsForValue();
    }

    @Bean(name = "userJsonObjHashRedisTemplate")
    public ValueOperations<String,JSONObject> createJsonObjHashRedisTemplate() {
//        RedisTemplate<String,JSONObject> templateCache = createTemplateCache(OperateEnum.DEFAULT);
//        return templateCache;
        return createTemplateCache(OperateEnum.DEFAULT).opsForValue();
    }
    @Bean(name = "userLiveDataObjHashRedisTemplate")
    public ValueOperations<String, LiveData> createLiveDataHashRedisTemplate() {
        return createTemplateCache(OperateEnum.DEFAULT).opsForValue();
    }

    @Bean(name = "userMemberDataObjHashRedisTemplate")
    public ValueOperations<String, MemberAudienceData> createMemberDataHashRedisTemplate() {
        return createTemplateCache(OperateEnum.DEFAULT).opsForValue();
    }

    @Bean(name = "defaultRedisTemplate")
    public RedisTemplate createDefaultRedisTemplate() {
        return createTemplateCache (OperateEnum.DEFAULT);
    }


    @Bean(name = "strRedisTemplate")
    public RedisTemplate createRedisTemplate() {
        return createTemplateCache (OperateEnum.STR);
    }

    @Bean(name = "hashRedisTemplate")
    public RedisTemplate createHashRedisTemplate() {
        return createTemplateCache (OperateEnum.HASH);
    }


}
