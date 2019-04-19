package com.store.enumerate;

import com.jiuyuan.util.enumeration.IntEnum;

/**
 * @author jeff.zhan
 * @version 2016年11月17日 下午7:16:31
 * 
 */

public enum OrderType implements IntEnum {
	NORMAL(0, "普通订单"),

	AFTERSALE(1, "售后订单");
	
	private OrderType(int intValue, String displayName) {
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
    
    public static OrderType getNameByValue(int intValue ) {
        if (intValue<0) {
            return null;
        }

        for (OrderType orderStatus : OrderType.values()) {
            if (orderStatus.getIntValue()==intValue) {
                return orderStatus;
            }
        }

        return null;
    }
}
