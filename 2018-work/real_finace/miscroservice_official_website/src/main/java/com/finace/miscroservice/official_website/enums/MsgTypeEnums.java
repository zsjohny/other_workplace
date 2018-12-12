package com.finace.miscroservice.official_website.enums;

public enum  MsgTypeEnums {
    NEWS_CENTER("公告中心",1),
    OFFICIAL_NOTICE("官方通知",2);
    private String value;
    private Integer code;

    MsgTypeEnums(String value, Integer code) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public Integer getCode() {
        return code;
    }
}
