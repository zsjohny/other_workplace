package com.reliable.util; /**
 *
 */

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @author wb-pengjian.d
 * @version $Id: QueryPhoneSetAttrImpl.java, 2015-10-21 下午5:37:34
 */

public class QueryPhoneSetAttr {


    public static void setSetConnecAttr(HttpURLConnection connection, String readPropertiesName) {
        try {

            if (readPropertiesName.equals("")) {
                return;
            }
            // 读取配置文件
            Properties properties = new Properties();
            String webUrl = System.getProperty("user.dir") + "\\WebRoot\\properties\\appLoading\\" + readPropertiesName;
            properties.load(new BufferedReader(new InputStreamReader(new FileInputStream(webUrl), "utf-8")));
            // 依次读取配置文件的内容
            Enumeration<?> enumeration = properties.propertyNames();
            while (enumeration.hasMoreElements()) {
                Object object = enumeration.nextElement();
                String str = (String) object;
                connection.setRequestProperty(str, properties.getProperty(str));
            }
        } catch (Exception e) {
        }

    }

}