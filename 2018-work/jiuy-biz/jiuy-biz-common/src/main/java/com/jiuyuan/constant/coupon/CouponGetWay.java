package com.jiuyuan.constant.coupon;

public enum CouponGetWay {
	GRANT(0, "发放"),
	
	FETCH(1, "领取"),
	
	INVITE(2, "邀请"),
	
	REGISTER(3, "注册"),
	
	DRAW_LOTTERY(4, "抽奖"),
	
	POINT_EXCHANGE(5, "积分兑换"),
	
	STORE_INVITE_USER(6,"门店邀请会员绑定"),
	
	FAVORITE_STORE(7,"用户关注门店");
	
	private Integer value;
	
	private String description;
	
	CouponGetWay(Integer value, String description) {
		this.value = value;
		this.description = description;
	}
	
	public Integer getValue() {
		return value;
	}
	
	public String getDescription() {
		return description;
	}
	
	public static RangeType getByValue(Integer value) {
		for (RangeType rangeType : RangeType.values()) {
			if (rangeType.getValue() == value) {
				return rangeType;
			}
		}
		return null;
	}
}
