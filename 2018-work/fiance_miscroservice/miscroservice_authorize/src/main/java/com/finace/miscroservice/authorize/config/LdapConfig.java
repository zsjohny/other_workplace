package com.finace.miscroservice.authorize.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * ldap的配置扫描类
 */
@Configuration
@ConfigurationProperties("ldap")
public class LdapConfig {
    public static final String DC_FILED = "domain";
    public static final String OBJECT_FILED = "person";

    /**
     * 数据连接的地址 可有多个 用,分割开
     */
    private String[] urls;
    /**
     * 数据连接的基准
     */
    private String base;


    /**
     * 数据连接的用户名
     */
    private String userName;

    /**
     * 数据连接的密码
     */
    private String userPass;

    /**
     * 数据的类型 多个利用,分割
     */
    private String[] objectClass;


    public String[] getUrls() {
        return urls;
    }

    public void setUrls(String[] urls) {
        this.urls = urls;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPass() {
        return userPass;
    }

    public void setUserPass(String userPass) {
        this.userPass = userPass;
    }

    public String[] getObjectClass() {
        return objectClass;
    }

    public void setObjectClass(String[] objectClass) {
        this.objectClass = objectClass;
    }
}
