package com.jiuy.rb.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 订单关闭类型枚举
 *
 * @author Aison
 * @version V1.0
 * @date 2018/6/28 16:21
 * @Copyright 玖远网络
 */
public enum  OrderCloseTypeEnum {


    NONE(0, "无需关闭"),

    BUYER_CLOSE(101, "买家主动取消订单"),

    UNPAID(102, "超时未付款系统自动关闭订单"),

    REFUND_ALL_CLOSE(103, "全部退款关闭订单"),

    SELLER_CLOSE(104, "卖家关闭订单"),

    PLATFORM_CLOSE(105, "平台客服关闭订单");

    private String name;

    private Integer code;

    OrderCloseTypeEnum(Integer code, String name){
        this.name = name;
        this.code = code;
    }

    private static Map<Integer,OrderCloseTypeEnum> enumMap = new HashMap<>(2);

    static {
        for (OrderCloseTypeEnum enumItem : OrderCloseTypeEnum.values()) {
            enumMap.put(enumItem.getCode(),enumItem);
        }
    }

    public  boolean isThis(Integer code) {

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
