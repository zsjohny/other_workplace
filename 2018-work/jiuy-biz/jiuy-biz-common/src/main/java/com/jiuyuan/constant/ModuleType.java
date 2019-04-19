package com.jiuyuan.constant;

import org.apache.commons.lang3.StringUtils;

import com.jiuyuan.util.enumeration.StringEnum;



public enum ModuleType implements StringEnum {
    HOT("hot"),

    SEASON("season"),

    CATEGORY("category"),

    ALL("all");

    private ModuleType(String stringValue) {
        this.stringValue = stringValue;
    }

    private String stringValue;

    @Override
    public String getStringValue() {
        return stringValue;
    }

    public boolean is(String stringValue) {
        return StringUtils.equals(stringValue, getStringValue());
    }

}
