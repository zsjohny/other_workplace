package com.jiuyuan.constant;

public enum Logistics {
	NO_DISCOUNT(0, "物流没有优惠"),
	DISCOUNT(1, "物流有优惠");
	
    private Logistics(int intValue, String description) {
        this.intValue = intValue;
        this.description = description;
    }

    private int intValue;

    private String description;

    public int getIntValue() {
        return intValue;
    }

    public String getDescription() {
        return description;
    }
}
