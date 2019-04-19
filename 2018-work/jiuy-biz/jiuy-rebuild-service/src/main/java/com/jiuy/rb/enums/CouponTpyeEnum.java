package com.jiuy.rb.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 优惠券枚举
 *  0红包,1优惠券,2打折券
 * @author HYQ
 * @version V1.0
 * @date 2018/8/19 9:16
 * @Copyright 玖远网络
 */
public enum CouponTpyeEnum {

    /**
     * 平台
     *
     */
    RED(0,"红包"),
    /**
     * 供应商
     *
     */
    COUPON(1,"优惠券"),

    /**
     * APP
     *
     */
    DISCOUNT(2,"打折券");


    private String name;

    private Integer code;

    CouponTpyeEnum(Integer code, String name){
        this.name = name;
        this.code = code;
    }

    private static Map<Integer,CouponTpyeEnum> enumMap = new HashMap<>(2);
    static {
        for (CouponTpyeEnum enumItem : CouponTpyeEnum.values()) {
            enumMap.put(enumItem.getCode(),enumItem);
        }
    }

    public static String getStatusName(Integer code) {
        CouponTpyeEnum enumItem = enumMap.get(code);
        return  enumItem == null ? "" : enumItem.getName();
    }

    public static CouponTpyeEnum get(Integer code) {
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
