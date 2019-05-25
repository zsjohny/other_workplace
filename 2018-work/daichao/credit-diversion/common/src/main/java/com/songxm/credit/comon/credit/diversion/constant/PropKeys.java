package com.songxm.credit.comon.credit.diversion.constant;

public interface PropKeys {
    //String JWT_USER_SECRETS = "jwt.user.secrets";
    //String JWT_SERVICE_SECRETS = "jwt.service.secrets";
    @Deprecated
    String SMS_CODE = "code";
    String SMS_CODE_NEW = "@@@@@@";
    String SMS_VALID_TIME = "validTime";

    String JWT_USER_ACTIVE_SECRET_VERSION = "jwt.user.activeSecretVersion";
    String JWT_SERVICE_ACTIVE_SECRET_VERSION = "jwt.service.activeSecretVersion";
    String JWT_TENANT_ACTIVE_SECRET_VERSION = "jwt.tenant.activeSecretVersion";
    String TENANT_TOKEN_EXPIRE_IN_DAY = "tenant.token.expire.in.day";
    String ORG_CODE = "123456";
}
