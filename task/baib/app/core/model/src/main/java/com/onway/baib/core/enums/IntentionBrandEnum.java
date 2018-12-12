package com.onway.baib.core.enums;

import org.apache.commons.lang.StringUtils;
/**
 * 保险意向品牌枚举类
 * 
 * @author weina
 * @version $Id: IntentionBrandEnum.java, v 0.1 2016年12月5日 上午11:23:53 ROG Exp $
 */
public enum IntentionBrandEnum {
    
    PICC("0", "中国人保"),
    
    CONTINENT("1", "大地保险"),
    
    PINGAN("2", "平安保险"),
    
    UNITED("3", "中华联合"),
    
    AXA("4", "安盛天平"),
    
    LIBERTY("5", "利宝车险"),
    
    ;
    
    /**
     * 通过枚举<code>code</code>获得枚举。
     * 
     * @param code         枚举编号
     * @return
     */
    public static IntentionBrandEnum getCommonStatusByCode(String code) {
        for (IntentionBrandEnum param : values()) {
            if (StringUtils.equals(param.getCode(), code)) {
                return param;
            }
        }
        return null;
    }
    
    private String code;
    
    private String message;

    private IntentionBrandEnum(String code, String message) {
        this.code = code;
        this.message = message;
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
