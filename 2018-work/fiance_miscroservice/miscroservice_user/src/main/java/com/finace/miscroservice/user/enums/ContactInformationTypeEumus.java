package com.finace.miscroservice.user.enums;

/**
 * 联系方式枚举
 */
public enum ContactInformationTypeEumus {
    PHONE(1,"Phone"),
    QQ(2,"QQ"),
    WE_CHAT(3,"WeChat");
    private Integer code;
    private String value;

    ContactInformationTypeEumus(Integer code, String value) {
        this.code = code;
        this.value = value;
    }

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
