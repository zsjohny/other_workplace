package com.jiuy.rb.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 优惠券使用范围枚举
 *
 * 1 全场可用，2指定商品可用 3指定分类可用
 *
 * @author Aison
 * @version V1.0
 * @date 2018/8/6 18:18
 * @Copyright 玖远网络
 */
public enum  CouponRangeEnum {

    /**
     * 全场可用
     *
     */
    ALL(1,"全场可用"),

    /**
     * 指定商品可用
     *
     */
    PRODUCT(2,"指定商品可用"),

    /**
     * 指定分类可用
     */
    CATEGORY(3,"指定分类可用");

    private String name;

    private Integer code;

    CouponRangeEnum(Integer code, String name){
        this.name = name;
        this.code = code;
    }

    private static Map<Integer,CouponRangeEnum> enumMap = new HashMap<>(2);
    static {
        for (CouponRangeEnum enumItem : CouponRangeEnum.values()) {
            enumMap.put(enumItem.getCode(),enumItem);
        }
    }

    public static String getStatusName(Integer code) {
        CouponRangeEnum enumItem = enumMap.get(code);
        return  enumItem == null ? "" : enumItem.getName();
    }

    public static CouponRangeEnum get(Integer code) {
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
