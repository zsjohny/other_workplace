package com.jiuyuan.constant.coupon;

public enum CouponPushStatus {
	
	FAIL(-2, "推送失败"),
	
	NO_PUSH(-1, "无需推送"),
	
	TO_BE_PUSHED(0, "待推送"),
	
	PUSHED(1, "已推送"),
	
	PUSHING(2, "正在推送");

	private int intValue;
	
	private String description;
	
	private CouponPushStatus(int intValue, String description) {
		this.intValue = intValue;
		this.description = description;
	}

	public int getIntValue() {
		return intValue;
	}

	public String getDescription() {
		return description;
	}
	
}
