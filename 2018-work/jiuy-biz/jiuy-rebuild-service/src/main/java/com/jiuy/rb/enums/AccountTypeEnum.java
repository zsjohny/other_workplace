package com.jiuy.rb.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 用户账户类型
 *
 * @author Aison
 * @version V1.0
 * @date 2018/7/5 10:00
 * @Copyright 玖远网络
 */
public enum AccountTypeEnum {

    /**
     * 登陆退出
     *
     */
    WXA_USER(1,"小程序用户"),
    /**
     * 物流发货
     */
    STORE_USER(2,"门店用户");

    private String name;

    private Integer code;

    AccountTypeEnum(Integer code, String name){
        this.name = name;
        this.code = code;
    }

    private static Map<Integer,AccountTypeEnum> enumMap = new HashMap<>(2);
    static {
        for (AccountTypeEnum enumItem : AccountTypeEnum.values()) {
            enumMap.put(enumItem.getCode(),enumItem);
        }
    }

    public static String getStatusName(Integer code) {
        AccountTypeEnum enumItem = enumMap.get(code);
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
