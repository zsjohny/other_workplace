package com.onway.baib.core.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 结算状态枚举类
 * 
 * @author jiaming.zhu
 * @version $Id: UserTypeEnum.java, v 0.1 2017年1月22日 上午11:31:44 ZJM Exp $
 */
public enum SettleStateEnum {

    WAIT_SETTLE("0", "待结算"),

    SETTLING("1", "待到账"),

    ALEARDY_SETTLE("2", "已结算"), ;

    private String code;

    private String message;

    /**
     * 通过枚举<code>code</code>获得枚举。
     * 
     * @param code         枚举编号
     * @return
     */
    public static SettleStateEnum getUserTypeEnumStatusByCode(String code) {
        for (SettleStateEnum param : values()) {
            if (StringUtils.equals(param.getCode(), code)) {
                return param;
            }
        }
        return null;
    }

    private SettleStateEnum(String code, String message) {
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
