package com.jiuyuan.constant;

public enum DiscountType {

    CATEGORY(0),

    BRAND(1);
    
    private int intValue;
    
    private DiscountType(int intValue) {
        this.intValue = intValue;
    }

    public int getIntValue() {
        return intValue;
    }
}
