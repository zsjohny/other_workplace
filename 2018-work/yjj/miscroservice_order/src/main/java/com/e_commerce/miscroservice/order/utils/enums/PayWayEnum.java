package com.e_commerce.miscroservice.order.utils.enums;

import java.util.HashMap;
import java.util.Map;

public enum PayWayEnum {

    /*
    *公众号
    */
    WX(1,"微信");

    private String name;

    private Integer code;

    PayWayEnum(Integer code , String name) {
        this.name = name;
        this.code = code;
    }

    private static Map<Integer,PayWayEnum> enumMap = new HashMap<>(2);

    static {
        for (PayWayEnum enumItem : PayWayEnum.values()) {
            enumMap.put(enumItem.getCode(),enumItem);
        }
    }

    public static String getStatusName(Integer code) {
        PayWayEnum enumItem = enumMap.get(code);
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
