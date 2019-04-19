package com.jfinal.utils;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/23 11:07
 * @Copyright 玖远网络
 */
public class ConfigUtil {
    /* 加载服务配置文件  */
    private static Prop prop = PropKit.use("memcached.properties");


    private ConfigUtil(){}
    public static String getMemcachedServers(String key){
       String servers = prop.get(key);
       return servers;
    }
}
