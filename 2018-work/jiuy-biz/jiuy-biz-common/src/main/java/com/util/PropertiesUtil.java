package com.util;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @Author hyf
 * @Date 2019/1/9 19:21
 */
public class PropertiesUtil {

    private static Properties config = new Properties();

    /**
     * 获取相应 properties 参数
     * @param key
     * @return
     */
    public static String getPropertiesByKey(String properties,String key){

        InputStream in = null;
        ClassPathResource resource = new ClassPathResource(properties);
        try {
            in= resource.getInputStream();
            config.load (in);
            String value = config.getProperty(key, "").trim();
            return value;
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (in!=null){
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
