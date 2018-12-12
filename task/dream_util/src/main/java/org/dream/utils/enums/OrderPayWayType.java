package org.dream.utils.enums;

/**
 * Created by nessary on 16-7-19.
 */
public enum OrderPayWayType {
    payWayByMoney("payWayByMoney", 1),
    payWayByPoint("payWayByPoint", 0);
    String key;
    Integer value;

    OrderPayWayType(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
