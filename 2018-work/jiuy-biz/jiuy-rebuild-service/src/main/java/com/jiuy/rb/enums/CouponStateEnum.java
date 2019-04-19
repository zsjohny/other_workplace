package com.jiuy.rb.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * -2删除 -1:作废  0:未用 1:已使用
 *
 * @author Aison
 * @version V1.0
 * @date 2018/8/6 15:27
 * @Copyright 玖远网络
 */
public enum CouponStateEnum {


    /**
     * 立即发放
     *
     */
    DEL(-2,"删除"),
    /**
     * 作废
     *
     */
    CANCEL(-1,"作废"),
    /**
     * 未使用
     */
    NOT_USE(0,"未使用"),

    /**
     * 已使用
     */
    USED(1,"已使用"),

    /**
     * 已失效
     */
    LOST(2,"已失效");


    private String name;

    private Integer code;

    CouponStateEnum(Integer code, String name){
        this.name = name;
        this.code = code;
    }

    private static Map<Integer,CouponStateEnum> enumMap = new HashMap<>(2);
    static {
        for (CouponStateEnum enumItem : CouponStateEnum.values()) {
            enumMap.put(enumItem.getCode(),enumItem);
        }
    }

    public static String getStatusName(Integer code) {
        CouponStateEnum enumItem = enumMap.get(code);
        return  enumItem == null ? "" : enumItem.getName();
    }

    public static CouponStateEnum get(Integer code) {
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
