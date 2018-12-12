package com.onway.baib.core.enums;

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
public enum PaymentStageEnum {

    EARNESTMONEY("PAYEARNESTMONEY","1", "付定金"),
    
    RETAINAGE("RETAINAGE","2", "付尾款"),
    
    OVERRUNCOST("OVERRUNCOST","3", "付超额费用"),;

    /** 枚举编号 */
    private String code;
    
    /**枚举值**/
    private String value;

    /** 枚举详情 */
    private String message;

    /**
     * 构造方法
     *
     * @param code         枚举编号
     * @param message      枚举详情
     */
    private PaymentStageEnum(String code, String value,String message) {
        this.code = code;
        this.setValue(value);
        this.message = message;
    }

    /**
     * 通过枚举<code>code</code>获得枚举。
     * 
     * @param code         枚举编号
     * @return
     */
    public static PaymentStageEnum getChannelEnumByCode(String code) {
        for (PaymentStageEnum param : values()) {
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
