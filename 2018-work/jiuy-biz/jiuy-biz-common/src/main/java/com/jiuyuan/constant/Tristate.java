package com.jiuyuan.constant;

import com.jiuyuan.util.enumeration.StringEnum;

public enum Tristate implements StringEnum {

    YES("yes"),

    NO("no"),

    UNCERTAIN("uncertain");

    private Tristate(String stringValue) {
        this.stringValue = stringValue;
    }

    private String stringValue;

    @Override
    public String getStringValue() {
        return stringValue;
    }
}
