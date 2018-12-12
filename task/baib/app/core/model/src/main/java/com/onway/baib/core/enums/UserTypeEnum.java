package com.onway.baib.core.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 用户类型枚举类
 * 
 * @author jiaming.zhu
 * @version $Id: UserTypeEnum.java, v 0.1 2017年1月22日 上午11:31:44 ZJM Exp $
 */
public enum UserTypeEnum {

    USER("0", "用户"),

    DRIVER("1", "司机"),

    BUSINESS("2", "商户"),

    INFORMATION_MANAGEMENET("3","资管"),

    CUSTOM_SERVICE("4","客服") ,

    SETTLEMENT_PERSONNEL("5","结算")

    ;

    private String code;

    private String message;

    /**
     * 通过枚举<code>code</code>获得枚举。
     * 
     * @param code         枚举编号
     * @return
     */
    public static UserTypeEnum getUserTypeEnumStatusByCode(String code) {
        for (UserTypeEnum param : values()) {
            if (StringUtils.equals(param.getCode(), code)) {
                return param;
            }
        }
        return null;
    }

    private UserTypeEnum(String code, String message) {
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
