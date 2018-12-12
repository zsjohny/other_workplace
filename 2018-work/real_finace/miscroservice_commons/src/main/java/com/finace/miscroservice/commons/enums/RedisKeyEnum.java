package com.finace.miscroservice.commons.enums;

/**
 * redis操作的key枚举类
 */
public enum RedisKeyEnum {

    /**
     * redis操作存储类的规范类
     * 公司名称:存储类型(hash/str/zset/..):存储的key名称(多个字母用_连接):key的类型(complete/prefix)
     */
    CODE_STR_REDIS_PREFIX_KEY("eTongJin:str:image_code:prefix:"),
    LIMITER_HASH_REDIS_PREFIX_KEY("eTongJin:hash:limiter_rate:prefix:"),
    //交易网关的redisKey的枚举
    GETWAY_DYNAMIC_CONFIG_KEY("eTongJin:hash:getway_dynamic_config:complete"),
    //用户登陆状态的枚举
    USER_LOAD_STATUS_KEY("eTongJin:hash:user_load_status:complete"),
    //公告中心
    NOTICE_KEY ( "message:center:notice:key"),

    //官方通知
    OFFICIAL_KEY("message:center:official:key");

    RedisKeyEnum(String key) {
        this.key = key;
    }

    private String key;

    public String toKey() {

        return key;
    }

}
