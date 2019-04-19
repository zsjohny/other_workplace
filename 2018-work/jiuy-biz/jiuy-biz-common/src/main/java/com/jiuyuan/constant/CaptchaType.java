package com.jiuyuan.constant;

import com.jiuyuan.util.enumeration.StringEnum;

public enum CaptchaType implements StringEnum {

    /** 登录 */
    LOGIN("login", 5, 120, 50, 5),

    /** 注册 */
    REGISTER("register", 5, 120, 50, 5),

    /** 重置密码 */
    RESET_PASSWORD("resetpwd", 5, 120, 50, 5);

    private CaptchaType(String stringValue, int length, int width, int height, int limitValidTime) {
        this.stringValue = stringValue;
        this.length = length;
        this.width = width;
        this.height = height;
        this.limitValidTime = limitValidTime;
    }

    private String stringValue;

    private int length;

    private int width;

    private int height;

    private int limitValidTime;

    public String getStringValue() {
        return stringValue;
    }

    public int getLength() {
        return length;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLimitValidTime() {
        return limitValidTime;
    }

    public static CaptchaType fromStringValue(String stringValue) {
        for (CaptchaType type : values()) {
            if (type.getStringValue().equals(stringValue)) {
                return type;
            }
        }

        throw new IllegalArgumentException(stringValue);
    }
}
