package com.jiuyuan.service.common.area;

import org.springframework.stereotype.Service;

/**
 * 目前小程序使用, 向rebuild工程的CacheService兼容
 *
 * @author Charlie
 * @version V1.0
 * @date 2018/8/2 15:33
 * @Copyright 玖远网络
 */
@Service
public class BizCacheServiceImpl implements IBizCacheService{


    /**
     * 存放一个简单的key value
     *
     * @param key key
     * @param value value
     * @return 是否成功
     * @author Charlie
     * @date 2018/8/2 15:52
     */
    @Override
    public Boolean setSimple(Object key, Object value) {
        return JedisUtils.setSimple (key, value);
    }


    /**
     * 存放一个简单的key value
     *
     * @param key key
     * @param value value
     * @param second 存活时间
     * @return 是否成功
     * @author Charlie
     * @date 2018/8/2 15:52
     */
    @Override
    public Boolean setSimple(Object key, Object value, int second) {
        return JedisUtils.setSimple (key, value, second);
    }


    /**
     * 存放一个简单的key value
     *
     * @param key key
     * @param value value
     * @param second 存活时间
     * @return 1 if the key was set 0 if the key was not set, -1 if failed
     * @author Charlie
     * @date 2018/8/2 15:52
     */
    @Override
    public Long setSimpleIfNoExist(Object key, Object value, int second) {
        return JedisUtils.setSimpleIfNoExist (key, value, second);
    }



    /**
     * 存放一个简单的key value
     *
     * @param key key
     * @param value value
     * @return 1 if the key was set 0 if the key was not set, 失败:null
     * @author Charlie
     * @date 2018/8/2 15:52
     */
    @Override
    public Long setSimpleIfNoExist(Object key, Object value) {
        return JedisUtils.setSimpleIfNoExist (key, value);
    }


    /**
     * 通过key获取值
     *
     * @param key key
     * @return java.lang.String
     * @author Charlie
     * @date 2018/8/2 15:53
     */
    @Override
    public String get(String key) {
        return JedisUtils.get (key);
    }

    /**
     * 减法
     *
     * @param key key
     * @param number number
     * @return java.lang.Long
     * @author Charlie
     * @date 2018/8/2 15:57
     */
    @Override
    public Long decr(String key, long number) {
        return JedisUtils.decrBy (key, number);
    }


    /**
     * 加法
     *
     * @param key key
     * @param number number
     * @return java.lang.Long
     * @author Charlie
     * @date 2018/8/2 15:57
     */
    @Override
    public Long incr(String key, long number) {
        return JedisUtils.incrBy (key, number);
    }


    /**
     * 存入一个复合对象
     *
     * @param key key
     * @param value value
     * @param second 存活时间
     * @return 是否成功
     * @author Charlie
     * @date 2018/8/2 16:25
     */
    @Override
    public Boolean setObject(String key, Object value, int second) {
        return JedisUtils.setObject (key, value, second);
    }
}
