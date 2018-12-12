package com.e_commerce.miscroservice.authorize.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * ldap的配置扫描类
 */
@Configuration
@ConfigurationProperties("ldap")
@Data
@configss
public class LdapConfig {
    public static final String DC_FILED = "domain";
    public static final String DEFAULT_UID = "0";
    public static final String DEFAULT_ID = "0";
    public static final String DEFAULT_ROLE = "*default*";
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


}
