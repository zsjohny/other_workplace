package com.goldplusgold.td.sltp.core.auth.cache;

import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;

/**
 * String类型的Redis缓存
 */
@Component
public class StringCacheService extends RedisCacheService<String> {

    @Override
    protected RedisSerializer<String> getSerializer() {
        return new StringRedisSerializer();
    }
}
