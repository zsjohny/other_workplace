package com.util;


import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.Properties;

/**
 * Jedis Cache 工具类
 *
 * @author Charlie(唐静)
 * @date 18/06/22
 */
public class ServerPathUtil{

    private static final String SERVER_PATH_PROPERTIES = "serverPath.properties";

    /**
     * 新系统分销系统地址
     */
    private String server2Distribution;


 /**
     * 新系统地址
     */
    private String server2Url;



    private ServerPathUtil() {
        InputStream in = null;
        try {
            // 获取配置信息
            ClassPathResource resource = new ClassPathResource (SERVER_PATH_PROPERTIES);
            in = resource.getInputStream ();
            Properties config = new Properties ();
            config.load (in);
            // 初始化配置
            initProperties (config);
            initPropertiesServer2Url (config);
        } catch (IOException e) {
            // ignore
        } finally {
            try {
                if (in != null) {
                    in.close ();
                }
            } catch (IOException ioe) {
                // ignore
            }
        }
    }


    public static ServerPathUtil me() {
        return Factory.INSTANCE;
    }

    private static class Factory{
        private static final ServerPathUtil INSTANCE = new ServerPathUtil ();
    }





    /**
     * 在这里做初始化properties
     *
     * @param config config
     * @author Charlie
     * @date 2018/10/30 11:08
     */
    private void initProperties(Properties config) {
        server2Distribution = config.getProperty ("server2distribution");

    }


    public String getServer2Distribution() {
        return server2Distribution;
    }
 /**
     * 在这里做初始化properties
     *
     * @param config config
     * @author Charlie
     * @date 2018/10/30 11:08
     */
    private void initPropertiesServer2Url(Properties config) {
        server2Url = config.getProperty ("server2Url");

    }


    public String getServer2Url() {
        return server2Url;
    }
}

