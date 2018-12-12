package org.dream.utils.enums;

/**
 * Created by nessary on 16-7-18.
 */
public enum OrderBuyDirectionType {
    BUY_UP("买涨", 1),

    BUY_OR("买跌", 0);

    String key;
    Integer value;

    OrderBuyDirectionType(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }


}
