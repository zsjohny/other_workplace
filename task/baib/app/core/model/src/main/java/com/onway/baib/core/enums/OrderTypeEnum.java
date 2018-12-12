package com.onway.baib.core.enums;

import org.apache.commons.lang.StringUtils;

/**
 * 订单状态枚举类
 * 
 * @author jiaming.zhu
 * @version $Id: OrderTypeEnum.java, v 0.1 2017年1月22日 下午6:43:23 ZJM Exp $
 */
public enum OrderTypeEnum {

    INIT("0", "初始化", "", ""),

    DOWNPAYMENT("1", "付定", "", ""),

    CONFIRM("2", "订单确认", "", ""),

    FEED_BACK("3", "已反馈", "", ""),

    WAIT_TURN_OUT("4", "待出车", "", ""),

    TURN_OUT_SIGN("5", "出车已签到", "车辆出库", "1"),

    TURN_OUT_DETECTION("6", "出车测车", "测车签字", "2"),

    TURN_OUT_ALEARDY_DETECTION("7", "出车已测车", "", "3"),

    PAY_MENT("8", "已付款", "", ""),

    TURN_OUT_ING("9", "出车中", "开始服务", "4"),

    SELF_END("10", "结束服务", "结束服务", "5"),

    OVER_ORDER("11", "订单超额", "超出订单", "6"),

    FINISH("12", "已完成", "", ""),

    BACK_DETECTION("13", "还车测车", "测车签字", "7"),

    BACK_ALEARDY_DETECTION("14", "还车已测车", "", ""),

    BACK_SIGN("15", "还车签到", "", ""),

    RETURN_CAR("16", "入库", "车辆入库", "8"),

    SETTLE("17", "已结算", "", ""), ;

    private String code;

    private String message;

    private String usingType;

    private String driverOrderType;

    /**
     * 通过枚举<code>code</code>获得枚举。
     * 
     * @param code         枚举编号
     * @return
     */
    public static OrderTypeEnum getOrderTypeEnumByCode(String code) {
        for (OrderTypeEnum param : values()) {
            if (StringUtils.equals(param.getCode(), code)) {
                return param;
            }
        }
        return null;
    }

    /**
     * 通过枚举<driverOrderType>driverOrderType</driverOrderType>获得枚举。
     * 
     * @param driverOrderType         枚举编号
     * @return
     */
    public static OrderTypeEnum getOrderTypeEnumBydriverOrderType(String driverOrderType) {
        for (OrderTypeEnum param : values()) {
            if (StringUtils.equals(param.getDriverOrderType(), driverOrderType)) {
                return param;
            }
        }
        return null;
    }

    private OrderTypeEnum(String code, String message, String usingType, String driverOrderType) {
        this.code = code;
        this.message = message;
        this.usingType = usingType;
        this.driverOrderType = driverOrderType;
    }

    public String getUsingType() {
        return usingType;
    }

    public void setUsingType(String usingType) {
        this.usingType = usingType;
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

    public String getDriverOrderType() {
        return driverOrderType;
    }

    public void setDriverOrderType(String driverOrderType) {
        this.driverOrderType = driverOrderType;
    }

}
