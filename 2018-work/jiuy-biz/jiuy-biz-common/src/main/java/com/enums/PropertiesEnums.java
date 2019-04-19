package com.enums;

/**
 * @Author hyf
 * @Date 2019/1/9 19:27
 */
public enum PropertiesEnums {
    PROPERTIES_CONSTANTS("/constants.properties"),
    CONSTANTS_SHOP_IN_ID("shop.in.id"),
    CONSTANTS_STORE_ID("in.shop.store.id"),
    PAGE_URL("page.url"),
    ;
    private String key;

    public String getKey() {
        return key;
    }

    PropertiesEnums(String key) {
        this.key = key;
    }}
