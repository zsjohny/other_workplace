package com.finace.miscroservice.commons.handler.impl;

import com.finace.miscroservice.commons.handler.DistributeLockHandler;
import com.finace.miscroservice.commons.log.Log;
import com.finace.miscroservice.commons.utils.ApplicationContextUtil;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;

import java.util.concurrent.TimeUnit;


/**
 * redis分布式锁处理器实现类
 */
public class RedisDistributeLockHandler implements DistributeLockHandler {
    private RedissonClient redissonClient;
    private Log log = Log.getInstance(RedisDistributeLockHandler.class);

    public RedisDistributeLockHandler() {
        ApplicationContextUtil.addTask(() -> {
            RedisProperties redisProperties = ApplicationContextUtil.getBean(RedisProperties.class);
            if (redisProperties == null) {
                throw new RuntimeException("environment cannot find redis config");
            }
            Config config = new Config();
            config.useSingleServer().setAddress("redis://" + redisProperties.getHost() + ":" + redisProperties.getPort()).
                    setPassword(redisProperties.getPassword()).
                    setDatabase(redisProperties.getDatabase());
            redissonClient = Redisson.create(config);

            log.info("distributeLock init success");

        });


    }

    private final ThreadLocal<RLock> lockCache = new ThreadLocal<>();


    private final Object locks = new Object();
    private final Object unLocks = new Object();

    @Override
    public <T> void lock(T key) {
         getLock(key, 0);

    }

    /**
     * 获取锁定
     *
     * @param key      锁的key
     * @param lockTime 锁的时间单位(毫秒)
     */
    private <T> void getLock(T key, long lockTime) {

        synchronized (locks) {
            try {
                RLock lock = redissonClient.getLock(String.valueOf(key));
                if (lockTime <= 0) {

                    lock.lock();
                } else {
                    lock.lock(lockTime, TimeUnit.MILLISECONDS);
                }

                lockCache.set(lock);
                log.info("key={}已经被线程={}加锁", key, Thread.currentThread().getName());
            } catch (Exception e) {

                log.error("key={}被线程={}加锁出错", key, Thread.currentThread().getName(), e);
            }
        }
    }

    @Override
    public <T> void tryLock(T key, long time) {
         getLock(key, time);
    }

    @Override
    public <T> void unlock(T key) {

        synchronized (unLocks) {
            try {
                RLock rLock = lockCache.get();

                if (rLock != null) {
                    rLock.unlock();
                    lockCache.remove();
                    log.info("key={}已经被线程={}解锁", key, Thread.currentThread().getName());
                }

            } catch (Exception e) {

                log.error("key={}被线程={}解锁出错", key, Thread.currentThread().getName(), e);
            }
        }


    }
}
