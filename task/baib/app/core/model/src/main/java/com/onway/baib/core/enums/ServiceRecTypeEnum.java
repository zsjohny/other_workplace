/**
 * onway.com Inc.
 * Copyright (c) 2016-2016 All Rights Reserved.
 */
package com.onway.baib.core.enums;

/**
 * @author senze.liu
 * @version $Id: ServiceTypeEnum.java, v 0.1 2016/9/9 15:44 senze.liu Exp $
 */
public enum ServiceRecTypeEnum {

    WX_REC_FLAG("WX_REC_FLAG", "维修推荐标识"),
    MR_REC_FLAG("MR_REC_FLAG", "美容推荐标识"),
    GZ_REC_FLAG("GZ_REC_FLAG", "改装推荐标识"),
    BY_REC_FLAG("BY_REC_FLAG", "保养推荐标识"),;

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
    private ServiceRecTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 通过枚举<code>code</code>获得枚举。
     *
     * @param code         枚举编号
     * @return
     */
    public static ServiceRecTypeEnum getServiceTypeEnumByCode(String code) {
        for (ServiceRecTypeEnum param : values()) {
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
