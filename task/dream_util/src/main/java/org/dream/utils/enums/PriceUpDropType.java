package org.dream.utils.enums;

/**
 * 止损还是止盈
 * Created by nessary on 16-8-31.
 */
public enum PriceUpDropType {

    PriceUP("止盈", 1),

    PriceDrop("止损", 2);

    private String key;
    private int value;

    PriceUpDropType(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    public int getValue() {
        return value;
    }


    public String getKey() {
        return key;
    }

}
