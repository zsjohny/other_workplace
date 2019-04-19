package com.jiuy.rb.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 优惠券系统枚举
 *
 * @author Aison
 * @version V1.0
 * @date 2018/8/3 9:16
 * @Copyright 玖远网络
 */
public enum  CouponSysEnum {

    /**
     * 门店宝
     *
     */
    APP(1,"门店宝"),
    /**
     * 小程序
     *
     */
    WXA(2,"小程序");


    private String name;

    private Integer code;

    CouponSysEnum(Integer code, String name){
        this.name = name;
        this.code = code;
    }

    private static Map<Integer,CouponSysEnum> enumMap = new HashMap<>(2);
    static {
        for (CouponSysEnum enumItem : CouponSysEnum.values()) {
            enumMap.put(enumItem.getCode(),enumItem);
        }
    }

    public static String getStatusName(Integer code) {
        CouponSysEnum enumItem = enumMap.get(code);
        return  enumItem == null ? "" : enumItem.getName();
    }

    public static CouponSysEnum get(Integer code) {
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
