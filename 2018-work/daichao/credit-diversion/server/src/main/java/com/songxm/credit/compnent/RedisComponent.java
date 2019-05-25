package com.songxm.credit.compnent;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@Order(-214783647)
public class RedisComponent {
    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    private ValueOperations<Object, Object> operations;

    @PostConstruct
    public void init() {
        operations = redisTemplate.opsForValue();
    }


    public void set(Object key, Object value, Long expire) {

            operations.set(key, value, expire, TimeUnit.SECONDS);

    }

    public Object get(Object key) {
        try {
            return operations.get(key);
        } catch (Throwable e) {
            log.error("从redis获取key[{}]值时异常:{}", key, ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    public void del(Object key){
        redisTemplate.delete(key);
    }
}
