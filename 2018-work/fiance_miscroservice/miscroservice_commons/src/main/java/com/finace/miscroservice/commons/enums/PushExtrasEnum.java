package com.finace.miscroservice.commons.enums;

/**
 * Created by hyf on 2018/3/5.
 *
 */
public enum PushExtrasEnum {
    LOGIN_ONLY("0","单点登录"),
    BUY_FIRST("1","第一次购买");
    private String code;
    private String value;

    PushExtrasEnum() {
    }

    PushExtrasEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
