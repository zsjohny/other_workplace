package com.jiuyuan.constant.coupon;

public enum RangeType {
	
	GENERAL(0, "通用"),
	
	CATEGORY(1, "分类"),
	
	LIMIT_ORDER(2, "限额订单"),
	
	LIMIT_PRODUCT(3, "限定商品"),
	
	FREE_POSTAGE(4, "免邮"),
	
	BRAND(5, "品牌");
	
	private int value;
	
	private String description;
	
	RangeType(int value, String description) {
		this.value = value;
		this.description = description;
	}
	
	public int getValue() {
		return value;
	}
	
	public String getDescription() {
		return description;
	}
	
	public static RangeType getByValue(int value) {
		for (RangeType rangeType : RangeType.values()) {
			if (rangeType.getValue() == value) {
				return rangeType;
			}
		}
		return null;
	}
	
}

