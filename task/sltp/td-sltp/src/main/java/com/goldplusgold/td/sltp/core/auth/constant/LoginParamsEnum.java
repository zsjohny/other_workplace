package com.goldplusgold.td.sltp.core.auth.constant;

/**
 * 登录时，必须要的相关常量
 */
public enum LoginParamsEnum {

    /**
     * 金专家用户ID
     */
    JZJUSERID("id"),

    /**
     * 金专家登录帐号,就是手机号码
     */
    JZJUSERNAME("uerName"),

    /**
     * TD的用户ID
     */
    TDUSERID("tdUserID"),

    /**
     * TD的客户号
     */
    TDACCTNO("acctNo"),

    /**
     * 黄金交易编码
     */
    TDCUSTID("custId"),

    /**
     * 客户端平台
     */
    PLATFORM("platform"),

    /**
     * 客户端ID
     */
    IMEI("imei"),

    /**
     * 推送ID
     */
    CLIENTID("clientId"),

    /**
     * 用户登录广西黄金后返回的sessionId
     */
    TDSESSIONID("sessionId"),

    /**
     * 用户登录广西黄金后返回的sessionId
     */
    TDSESSIONKEY("sessionKey");


    private String name;

    LoginParamsEnum(String name) {
        this.name = name;
    }

    public String toName() {
        return this.name;
    }
}
