package com.jiuy.rb.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 优惠券枚举
 *  0 平台  1 供应商  2APP
 * @author HYQ
 * @version V1.0
 * @date 2018/8/19 9:16
 * @Copyright 玖远网络
 */
public enum CouponPlatEnum {

    /**
     * 平台
     *
     */
    PLAT(0,"平台"),
    /**
     * 供应商
     *
     */
    SUPPLIER(1,"供应商"),

    /**
     * APP
     *
     */
    APP(2,"APP");


    private String name;

    private Integer code;

    CouponPlatEnum(Integer code, String name){
        this.name = name;
        this.code = code;
    }

    private static Map<Integer,CouponPlatEnum> enumMap = new HashMap<>(2);
    static {
        for (CouponPlatEnum enumItem : CouponPlatEnum.values()) {
            enumMap.put(enumItem.getCode(),enumItem);
        }
    }

    public static String getStatusName(Integer code) {
        CouponPlatEnum enumItem = enumMap.get(code);
        return  enumItem == null ? "" : enumItem.getName();
    }

    public static CouponPlatEnum get(Integer code) {
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
