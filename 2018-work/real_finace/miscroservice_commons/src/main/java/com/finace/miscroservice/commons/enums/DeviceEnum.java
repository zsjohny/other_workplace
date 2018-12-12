package com.finace.miscroservice.commons.enums;

/**
 * 设备标识
 */
public enum DeviceEnum {

    ANDROID("a"), IOS("i"), H5("h");
    private String val;

    DeviceEnum(String val) {
        this.val = val;
    }

    public String toVal() {
        return val;
    }
}
