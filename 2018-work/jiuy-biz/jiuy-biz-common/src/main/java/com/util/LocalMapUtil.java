package com.util;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.jiuyuan.entity.ucpaas.SysConfig;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/8/18 10:56
 * @Copyright 玖远网络
 */
public class LocalMapUtil {
    private static int TIME_OUT= SysConfig.getInstance().getPropertyInt("local_map_time_out");
    //10分钟过期时间 Map
    private static LoadingCache<String,String> LOCAL_MAP=
            CacheBuilder.newBuilder().expireAfterAccess(TIME_OUT, TimeUnit.MINUTES).build(new CacheLoader<String, String>() {
                @Override
                public String load(String s) throws Exception {
                    return "";
                }
            });

    /**
     * 清空 Map
     * @param key
     */
    public static void invalidate(String key){
        LOCAL_MAP.invalidate(key);
    }

    /**
     *  根据key获取value
     * @param key
     * @return
     */
    public static String get(String key){
        String value = null;
        try {
            value = LOCAL_MAP.get(key);
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return value;
    }

    /**
     *  存储key value
     * @param key
     * @param value
     */
    public static void put(String key,String value){
        LOCAL_MAP.put(key,value);
    }
}
