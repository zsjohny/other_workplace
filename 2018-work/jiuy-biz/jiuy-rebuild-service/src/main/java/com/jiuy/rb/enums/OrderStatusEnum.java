package com.jiuy.rb.enums;

import io.swagger.models.auth.In;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单状态枚举
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/28 15:46
 * @Copyright 玖远网络
 */
public enum  OrderStatusEnum {

    UNPAID(0, "未付款"),

    ALL(5, "所有"),

    PAID(10, "已付款"),

    UNCHECK(20, "待审核"),

    CHECKED(30, "已审核"),

    CHECK_FAIL(40, "审核不通过"),

    DELIVER(50, "已发货"),

    SIGNED(60, "已签收"),

    SUCCESS(70, "交易成功"),

    REFUNDING(80, "退款中"),

    REFUNDED(90, "退款成功"),

    CLOSED(100, "交易关闭");


    private String name;

    private Integer code;

    OrderStatusEnum(Integer code, String name){
        this.name = name;
        this.code = code;
    }

    private static Map<Integer,OrderStatusEnum> enumMap = new HashMap<>(2);

    static {
        for (OrderStatusEnum enumItem : OrderStatusEnum.values()) {
            enumMap.put(enumItem.getCode(),enumItem);
        }
    }

    public static String getStatusName(Integer code) {
        OrderStatusEnum enumItem = enumMap.get(code);
        return  enumItem == null ? "" : enumItem.getName();
    }

    public boolean isThis(Integer code) {

        return this.getCode().equals(code);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
