package com.jiuyuan.constant.order;

import com.jiuyuan.util.enumeration.IntEnum;

public enum OrderCouponStatus implements IntEnum {

    SCRAP(-1, "作废"),
    
    UNUSED(0, "未用"),

    USED(1, "已用");
	
	

    private OrderCouponStatus(int intValue, String displayName) {
        this.intValue = intValue;
        this.displayName = displayName;
    }

    private int intValue;

    private String displayName;

    @Override
    public int getIntValue() {
        return intValue;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean is(int intValue) {
        return getIntValue() == intValue;
    }
    
    public static OrderCouponStatus getNameByValue(int intValue ) {
        if (intValue<0) {
            return null;
        }

        for (OrderCouponStatus orderStatus : OrderCouponStatus.values()) {
            if (orderStatus.getIntValue()==intValue) {
                return orderStatus;
            }
        }

        return null;
    }
    
    

    public static OrderCouponStatus parse(int intValue, OrderCouponStatus defaultValue) {
        for (OrderCouponStatus status : values()) {
            if (status.is(intValue)) {
                return status;
            }
        }
        return defaultValue;
    }
}
