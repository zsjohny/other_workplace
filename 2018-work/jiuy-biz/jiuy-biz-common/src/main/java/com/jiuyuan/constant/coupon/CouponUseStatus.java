package com.jiuyuan.constant.coupon;

import com.jiuyuan.util.enumeration.IntEnum;

public enum CouponUseStatus implements IntEnum {
	
	USED(0, "使用"),
	
    GIVE_BACK(1, "订单取消归还");

    private CouponUseStatus(int intValue, String displayName) {
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

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

    
    
}
