package com.e_commerce.miscroservice.order.utils.enums;

import java.util.HashMap;
import java.util.Map;

public enum OrderSourceEnum {

    /*
    *公众号
    */
    GZH(1,"公众号");

    private String name;

    private Integer code;

    OrderSourceEnum(Integer code ,String name) {
        this.name = name;
        this.code = code;
    }

    private static Map<Integer,OrderSourceEnum> enumMap = new HashMap<>(2);

    static {
        for (OrderSourceEnum enumItem : OrderSourceEnum.values()) {
            enumMap.put(enumItem.getCode(),enumItem);
        }
    }

    public static String getStatusName(Integer code) {
        OrderSourceEnum enumItem = enumMap.get(code);
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
