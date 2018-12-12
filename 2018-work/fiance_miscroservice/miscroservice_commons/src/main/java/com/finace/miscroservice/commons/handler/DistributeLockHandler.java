package com.finace.miscroservice.commons.handler;

/**
 * 分布式锁处理器
 */
public interface DistributeLockHandler {

    /**
     * 锁定
     *
     * @param key 锁定的参数
     */
    <T> void lock(T key);

    /**
     * 锁定
     *
     * @param key  锁定的参数
     * @param time 锁定的时间
     */
    <T> void tryLock(T key, long time);

    /**
     * 锁定
     *
     * @param key 解锁的参数
     */
    <T> void unlock(T key);

}
