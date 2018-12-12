/**
 * onway.com Inc.
 * Copyright (c) 2016-2016 All Rights Reserved.
 */
package com.onway.baib.core.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 会员注册设备类型
 * 
 * @author guangdong.li
 * @version $Id: RegisterFromTypeEnum.java, v 0.1 2014-11-19 下午8:10:41 guangdong.li Exp $
 */
public enum RegisterFromTypeEnum {

    /** PC端 */
    PC("PC", "官方网站", 1),

    /** 安卓客户端 */
    ANDROID("APP_AND", "安卓客户端", 2),

    /** 苹果客户端 */
    IOS("APP_IOS", "苹果客户端", 3),

    /** WP客户端*/
    WINDOW_PHONE("WP", "Windows Phone客户端", 4),

    /** 快速注册 */
    QUICK("QUICK", "快速注册", 5),
    
    /** 好友邀请 */
    INVITE("INVITE", "好友邀请", 6),

    /** 未知渠道 */
    UNKNOW("UNKNOW", "未知渠道", 0);

    /** 枚举编号 */
    private String code;

    /** 枚举详情 */
    private String message;

    /** 枚举编号 */
    private int    value;

    /**
     * 构造方法
     * 
     * @param code         枚举编号
     * @param message      枚举详情
     */
    private RegisterFromTypeEnum(String code, String message, int value) {
        this.code = code;
        this.message = message;
        this.value = value;
    }

    /**
     * 通过枚举<code>code</code>获得枚举。
     * 
     * @param code         枚举编号
     * @return
     */
    public static RegisterFromTypeEnum getFromTypeByCode(String code) {
        for (RegisterFromTypeEnum param : values()) {
            if (StringUtils.equals(param.getCode(), code)) {
                return param;
            }
        }
        return UNKNOW;
    }

    /**
     * 通过枚举<code>name</code>获得枚举。
     * 
     * @param name         枚举名称
     * @return
     */
    public static RegisterFromTypeEnum getFromTypeByName(String name) {
        for (RegisterFromTypeEnum param : values()) {
            if (StringUtils.equals(param.name(), name)) {
                return param;
            }
        }
        return UNKNOW;
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

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

}
