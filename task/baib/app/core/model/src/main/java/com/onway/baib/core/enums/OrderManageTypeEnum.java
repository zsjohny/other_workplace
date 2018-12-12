package com.onway.baib.core.enums;

import org.apache.commons.lang.StringUtils;


/**
 * 订单管理状态枚举类 
 * @author wenqiang.Wang
 * @version $Id: OrderManageTypeEnum.java, v 0.1 2017年2月7日 上午11:46:23 wenqiang.Wang Exp $
 */
public enum OrderManageTypeEnum {

    ALLORDER("0", "全部订单"),

    UNPAYMENT("1", "未付定"),

    WAIT_USER("2", "待用车"),

    TURN_OUT_ING("3", "用车中"),

    FINISH("4", "已完成"),;

    private String code;

    private String message;

    /**
     * 通过枚举<code>code</code>获得枚举。
     * 
     * @param code         枚举编号
     * @return
     */
    public static OrderManageTypeEnum getOrderTypeEnumByCode(String code) {
        for (OrderManageTypeEnum param : values()) {
            if (StringUtils.equals(param.getCode(), code)) {
                return param;
            }
        }
        return null;
    }

    private OrderManageTypeEnum(String code, String message) {
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
