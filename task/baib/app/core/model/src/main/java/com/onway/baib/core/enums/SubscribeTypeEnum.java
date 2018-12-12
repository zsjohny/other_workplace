package com.onway.baib.core.enums;

import com.onway.common.lang.StringUtils;

/**
 * 预约类型枚举
 * 
 * @author jiaming.zhu
 * @version $Id: SubscribeTypeEnum.java, v 0.1 2016年9月13日 下午3:28:48 zjm Exp $
 */
public enum SubscribeTypeEnum {

    WX("WX", "维修"),

    GZ("GZ", "改装"), ;

    /**枚举编码**/
    private String code;

    /**枚举详情**/
    private String message;

    /**
     * 构造函数
     * @param code
     * @param message
     */
    private SubscribeTypeEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 通过枚举编码获取枚举
     * 
     * @param code
     * @return
     */
    public static SubscribeTypeEnum getSubscribeTypeEnumByCode(String code) {
        for (SubscribeTypeEnum param : values()) {

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
