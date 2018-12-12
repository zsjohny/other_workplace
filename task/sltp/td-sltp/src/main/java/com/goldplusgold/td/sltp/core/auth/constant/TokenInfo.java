package com.goldplusgold.td.sltp.core.auth.constant;

/**
 * 与jwt token相关的常量
 */
public class TokenInfo {

    /**
     * 过期参数
     */
    public static final String EXPIRATION = "expiration";

    /**
     * HTTP header中的Authorization参数
     */
    public static final String HTTP_HEADER_AUTHORIZATION = "Authorization";

    /**
     * http响应中返回的token名称, 目前也会作为td的h5的cookie名称
     */
    public static final String HTTP_TD_RESPONSE_HEADER_TOKEN_NAME = "td-access-token";

    /**
     * HTTP header中的Authorization参数的Bearer认证类型
     */
    public static final String HTTP_HEADER_AUTHORIZATION_BEARER = "Bearer";


    public static final String HTTP_HEADER_JZJ_LOGIN_TIME_STAMP = "jzjLoginTime";
}
