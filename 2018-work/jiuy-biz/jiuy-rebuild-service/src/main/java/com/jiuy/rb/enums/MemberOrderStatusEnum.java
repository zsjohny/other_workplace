package com.jiuy.rb.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 小程序订单状态枚举
 *  订单状态：0:待付款;1:待提货;2:退款中;3:订单关闭;4:订单完成;5:待发货;6:已发货
 *
 * @author Aison
 * @version V1.0
 * @date 2018/7/23 9:31
 * @Copyright 玖远网络
 */
public enum  MemberOrderStatusEnum {


    /**
     * 待付款
     */
    UNPAID(0, "待付款"),

    /**
     * 待提货
     */
    WAIT_PICK_UP(1, "待提货"),

    /**
     * 退款中
     */
    REFUNDING(2, "退款中"),

    /**
     * 订单关闭
     */
    CLOSED(3, "订单关闭"),

    /**
     * 订单完成
     */
    SUCCESS(4,"订单完成"),

    /**
     * 已发货
     */
    DELIVER(6, "已发货"),

    /**
     * 待发货
     */
     PENDING_DELIVERY(5,"待发货");


    private String name;

    private Integer code;

    MemberOrderStatusEnum(Integer code, String name){
        this.name = name;
        this.code = code;
    }

    private static Map<Integer,MemberOrderStatusEnum> enumMap = new HashMap<>(2);

    static {
        for (MemberOrderStatusEnum enumItem : MemberOrderStatusEnum.values()) {
            enumMap.put(enumItem.getCode(),enumItem);
        }
    }

    public static String getStatusName(Integer code) {
        MemberOrderStatusEnum enumItem = enumMap.get(code);
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
