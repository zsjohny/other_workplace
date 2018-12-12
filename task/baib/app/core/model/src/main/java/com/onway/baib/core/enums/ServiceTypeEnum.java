/**
 * onway.com Inc.
 * Copyright (c) 2016-2016 All Rights Reserved.
 */
package com.onway.baib.core.enums;

/**
 * @author senze.liu
 * @version $Id: ServiceTypeEnum.java, v 0.1 2016/9/9 15:44 senze.liu Exp $
 */
public enum ServiceTypeEnum {

    WX("WX", "维修"),
    MR("MR", "美容"),
    GZ("GZ", "改装"),
    XC("XC", "洗车"),
    BY("BY", "保养"),
    BY_1("BY_1", "更换机油，机滤"),
    BY_2("BY_2", "更换空气滤清器"),
    BY_3("BY_3", "更换燃油滤清器（外置）"),
    BY_4("BY_4", "更换空调滤清器"),
    BY_5("BY_5", "更换空调制冷剂"),
    BY_6("BY_6", "更换刹车油"),
    BY_7("BY_7", "更换PM2.5滤芯"),
    BY_8("BY_8", "发动机内部清洗"),;

    /** 枚举编号 */
    private String code;

    /** 枚举详情 */
    private String message;

    /**
     * 构造方法
     *
     * @param code         枚举编号
     * @param message      枚举详情
     */
    private ServiceTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 通过枚举<code>code</code>获得枚举。
     *
     * @param code         枚举编号
     * @return
     */
    public static ServiceTypeEnum getServiceTypeEnumByCode(String code) {
        for (ServiceTypeEnum param : values()) {
            if (org.apache.commons.lang.StringUtils.equals(param.getCode(), code)) {
                return param;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
