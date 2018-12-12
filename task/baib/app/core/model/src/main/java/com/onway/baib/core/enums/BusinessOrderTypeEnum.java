package com.onway.baib.core.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 用户类型枚举类
 * 
 * @author jiaming.zhu
 * @version $Id: UserTypeEnum.java, v 0.1 2017年1月22日 上午11:31:44 ZJM Exp $
 */
public enum BusinessOrderTypeEnum {

    ALL("0", "全部"),

    WAIT_OUT("1", "待出车"),

    OUTING("2", "出车中"),

    FINISH("3", "已完成"),

    ;

    private String code;

    private String message;

    /**
     * 通过枚举<code>code</code>获得枚举。
     * 
     * @param code         枚举编号
     * @return
     */
    public static BusinessOrderTypeEnum getBusinessOrderTypeEnumByCode(String code) {
        for (BusinessOrderTypeEnum param : values()) {
            if (StringUtils.equals(param.getCode(), code)) {
                return param;
            }
        }
        return null;
    }

    private BusinessOrderTypeEnum(String code, String message) {
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
