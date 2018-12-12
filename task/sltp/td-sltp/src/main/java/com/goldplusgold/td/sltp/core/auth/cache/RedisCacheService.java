package com.goldplusgold.td.sltp.core.auth.cache;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis缓存抽象
 */
abstract class RedisCacheService<V> implements ICacheService<V> {

    private RedisTemplate<String, V> redisTemplate;
    private ValueOperations<String, V> valueOperations;

    private JedisConnectionFactory jedisFactory;
    private final long defaultLiveTime = 3600;
    private final long defaultIncrement = 1;

    @PostConstruct
    public void init() {
        redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(jedisFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(getSerializer());
        redisTemplate.setDefaultSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();

        valueOperations = redisTemplate.opsForValue();
    }

    @Autowired
    public void setJedisFactory(JedisConnectionFactory jedisFactory) {
        this.jedisFactory = jedisFactory;
    }

    protected abstract RedisSerializer<V> getSerializer();

    @Override
    public void del(String... keys) {
        redisTemplate.delete(Arrays.asList(keys));
    }

    @Override
    public void set(String key, V value, long liveTime) {
        valueOperations.set(key, value, liveTime, TimeUnit.SECONDS);
    }

    @Override
    public void setEX(String key, V value) {
        set(key, value, defaultLiveTime);
    }

    @Override
    public void set(String key, V value) {
        valueOperations.set(key, value);
    }

    @Override
    public V get(String key) {
        return valueOperations.get(key);
    }

    @Override
    public boolean exists(String key) {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> connection.exists(key.getBytes()));
    }

    @Override
    public boolean flushDB() {
        return redisTemplate.execute((RedisCallback<Boolean>) connection -> {
            connection.flushDb();
            return true;
        });
    }

    @Override
    public Long dbSize() {
        return redisTemplate.execute((RedisCallback<Long>) connection -> connection.dbSize());
    }

    @Override
    public String ping() {
        return redisTemplate.execute((RedisCallback<String>) connection -> connection.ping());
    }

    @Override
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    @Override
    public List<V> values(String pattern) {
        return valueOperations.multiGet(keys(pattern));
    }

    @Override
    public void mset(Map<String, V> map) {
        valueOperations.multiSet(map);
    }

    @Override
    public List<V> mget(String[] keys) {
        return valueOperations.multiGet(Arrays.asList(keys));
    }

    @Override
    public long getIncreValue(String key) {
        return valueOperations.increment(key, defaultIncrement);
    }
}
