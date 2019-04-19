package com.jiuy.rb.enums;

import java.util.HashMap;
import java.util.Map;

/**
 *  提现状态:1 提现中,2提现失败，3提现成功
 *
 * @author Aison
 * @version V1.0
 * @date 2018/7/18 15:44
 * @Copyright 玖远网络
 */
public enum  CashOutStatusEnum {


    /**
     * 提现中
     *
     */
    WAI_OUT(1,"提现中"),
    /**
     * 提现失败
     */
    FAILED(2,"提现失败"),

    /**
     *3提现成功
     */
    SUCCESS(3,"提现成功");


    private String name;

    private Integer code;

    CashOutStatusEnum(Integer code, String name){
        this.name = name;
        this.code = code;
    }

    private static Map<Integer,CashOutStatusEnum> enumMap = new HashMap<>(2);
    static {
        for (CashOutStatusEnum enumItem : CashOutStatusEnum.values()) {
            enumMap.put(enumItem.getCode(),enumItem);
        }
    }

    public static String getStatusName(Integer code) {
        CashOutStatusEnum enumItem = enumMap.get(code);
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
