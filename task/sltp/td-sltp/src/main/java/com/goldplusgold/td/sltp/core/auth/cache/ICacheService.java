package com.goldplusgold.td.sltp.core.auth.cache;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 缓存接口
 */
public interface ICacheService<V> {

    /**
     * 通过key删除
     */
    void del(String... keys);

    /**
     * 添加key value 并且设置存活时间
     */
    void set(String key, V value, long liveTime);

    /**
     * 添加key value,并设置默认过期时间
     */
    void setEX(String key, V value);

    /**
     * 添加没有规定过期时间的 key value
     */
    void set(String key, V value);

    /**
     * 获取cache value
     */
    V get(String key);

    boolean exists(String key);

    /**
     * 清空cache 所有数据
     */
    boolean flushDB();

    /**
     * 查看cache里有多少数据
     */
    Long dbSize();

    /**
     * 检查连接
     */
    String ping();

    /**
     * 查询所有匹配的keys
     */
    Set<String> keys(String pattern);

    /**
     * 获取匹配的value
     */
    List<V> values(String pattern);

    /**
     * 批量set
     */
    void mset(Map<String, V> map);

    /**
     * 批量get
     */
    List<V> mget(String[] keys);

    /**
     * 获取通过increment()自增的值
     */
    long getIncreValue(String key);

}
