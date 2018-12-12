package com.finace.miscroservice.official_website.enums;

/**
 * Created by hyf on 2018/3/5.
 */
public enum NewUserEnums {
    IS_NEW_TRUE(1,"新用户"),
    IS_NEW_FALSE(0,"老用户");
    private Integer code;
    private String value;

    public Integer getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    NewUserEnums(Integer code, String value) {
        this.code = code;
        this.value = value;
    }
}
