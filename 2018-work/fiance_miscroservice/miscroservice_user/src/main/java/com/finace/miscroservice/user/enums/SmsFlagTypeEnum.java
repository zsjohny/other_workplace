package com.finace.miscroservice.user.enums;

public enum SmsFlagTypeEnum {
    SEND("发短信","1"),NOT_SEND("不发短信","0");
    private String value;
    private String code;

    public String getValue() {
        return value;
    }

    public String getCode() {
        return code;
    }

    SmsFlagTypeEnum(String value, String code) {
        this.value = value;
        this.code = code;
    }
}
