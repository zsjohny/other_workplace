package com.onway.baib.core.enums;

import org.apache.commons.lang.StringUtils;


/**
 * 产品类型枚举
 * @author wenqiang.Wang
 * @version $Id: ProductTypeEnum.java, v 0.1 2017年2月7日 下午3:30:24 wenqiang.Wang Exp $
 */
public enum ProductTypeEnum {

    WEDDINGCAR("0", "婚车"),

    VEHICLEPACKAGE("1", "套餐"),

    BUSINESS("2", "商务用车"),;

    private String code;

    private String message;

    /**
     * 通过枚举<code>code</code>获得枚举。
     * 
     * @param code         枚举编号
     * @return
     */
    public static ProductTypeEnum getOrderTypeEnumByCode(String code) {
        for (ProductTypeEnum param : values()) {
            if (StringUtils.equals(param.getCode(), code)) {
                return param;
            }
        }
        return null;
    }

    private ProductTypeEnum(String code, String message) {
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
