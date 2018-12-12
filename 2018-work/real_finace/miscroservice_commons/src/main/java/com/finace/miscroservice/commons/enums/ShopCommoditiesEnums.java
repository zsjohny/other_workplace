package com.finace.miscroservice.commons.enums;

public enum  ShopCommoditiesEnums {
    DEFAULT("商城商品兑换，消耗%s金豆",1),
    OTHERS("商城商品兑换，消耗%s金豆",-1);
    private String value;
    private Integer code;

    ShopCommoditiesEnums(String value, Integer code) {
        this.value = value;
        this.code = code;
    }

    public String getValue() {
        return value;
    }

    public Integer getCode() {
        return code;
    }
}
