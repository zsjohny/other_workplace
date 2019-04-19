package com.e_commerce.miscroservice.order.utils.enums;

import java.util.HashMap;
import java.util.Map;

public enum OrderStatusEnum {

    PROCESS(0,"处理中"),

    SUCCESS(1,"成功"),

    FAIL(2,"失败"),

    NEW(9,"新订单");

    private String name;

    private Integer code;

    OrderStatusEnum(Integer code , String name) {
        this.name = name;
        this.code = code;
    }

    private static Map<Integer,OrderStatusEnum> enumMap = new HashMap<>(2);

    static {
        for (OrderStatusEnum enumItem : OrderStatusEnum.values()) {
            enumMap.put(enumItem.getCode(),enumItem);
        }
    }

    public static String getStatusName(Integer code) {
        OrderStatusEnum enumItem = enumMap.get(code);
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
