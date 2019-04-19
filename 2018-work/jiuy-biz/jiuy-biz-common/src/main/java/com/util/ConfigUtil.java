package com.util;


import java.io.*;
import java.util.Iterator;
import java.util.Properties;

/**
 * @author hyf
 * @version V1.0
 * @date 2018/11/23 11:07
 * @Copyright 玖远网络
 */
public class ConfigUtil {
    /* 加载服务配置文件  */
    private ConfigUtil(){}
    //读取缓存配置
    public static String getMemcachedServers(String key){

        Properties prop = new Properties();
        try{
            //读取属性文件memcached.properties
            InputStream in = ConfigUtil.class.getClassLoader().getResourceAsStream("memcached.properties");
//            InputStream in = new BufferedInputStream(new FileInputStream("memcached.properties"));
            prop.load(in);     ///加载属性列表
//            Iterator<String> it=prop.stringPropertyNames().iterator();
//            while(it.hasNext()){
//                String key=it.next();
//                System.out.println(key+":"+prop.getProperty(key));
//            }
            in.close();


        }
        catch(Exception e){
            System.out.println(e);
        }
        return prop.getProperty(key);
    }

}
