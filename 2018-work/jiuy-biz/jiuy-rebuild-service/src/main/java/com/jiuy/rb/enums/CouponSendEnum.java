package com.jiuy.rb.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 优惠券发放类型枚举
 * 0 立即发放,1自助领取,2新用户注册,3用户购买
 *
 * @author Aison
 * @version V1.0
 * @date 2018/8/2 14:36
 * @Copyright 玖远网络
 */
public enum  CouponSendEnum {


    /**
     * 立即发放
     *
     */
    SEND_NOW(0,"立即发放"),
    /**
     * 自助领取
     *
     */
    RECEIVE_SELF(1,"自助领取"),

    /**
     * 用户注册
     */
    REGISTER(2,"用户注册"),

    /**
     * 用户购买订单
     */
    ORDER(3,"用户购买订单");


    private String name;

    private Integer code;

    CouponSendEnum(Integer code, String name){
        this.name = name;
        this.code = code;
    }

    private static Map<Integer,CouponSendEnum> enumMap = new HashMap<>(2);
    static {
        for (CouponSendEnum enumItem : CouponSendEnum.values()) {
            enumMap.put(enumItem.getCode(),enumItem);
        }
    }

    public static String getStatusName(Integer code) {
        CouponSendEnum enumItem = enumMap.get(code);
        return  enumItem == null ? "" : enumItem.getName();
    }

    public static CouponSendEnum get(Integer code) {
       return enumMap.get(code);
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
