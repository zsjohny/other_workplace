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
public enum CleanTypeEnum {

    MPX("0", "人工普洗"),
    MJX("1", "人工精洗"),
    CJX("2", "电脑普洗"),;

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
    private CleanTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 通过枚举<code>code</code>获得枚举。
     * 
     * @param code         枚举编号
     * @return
     */
    public static CleanTypeEnum getChannelEnumByCode(String code) {
        for (CleanTypeEnum param : values()) {
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
