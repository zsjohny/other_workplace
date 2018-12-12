package com.onway.baib.core.annotation.enums;

/**
 * onway.com Inc.
 * Copyright (c) 2013-2014 All Rights Reserved.
 */

import org.apache.commons.lang.StringUtils;

/**
 * 终端枚举类
 * 
 * @author li.hong
 * @version $Id: TerminalTypeEnum.java, v 0.1 2016年9月1日 下午3:23:03 li.hong Exp $
 */
public enum CleanSubTypeEnum {

    FIVE_CHAIR("0", "五座"),
    SEVEN_CHAIR("1", "七座"),;

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
    private CleanSubTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 通过枚举<code>code</code>获得枚举。
     * 
     * @param code         枚举编号
     * @return
     */
    public static CleanSubTypeEnum getChannelEnumByCode(String code) {
        for (CleanSubTypeEnum param : values()) {
            if (StringUtils.equals(param.getCode(), code)) {
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
