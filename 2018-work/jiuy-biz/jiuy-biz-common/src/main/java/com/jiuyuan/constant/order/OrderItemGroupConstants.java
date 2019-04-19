package com.jiuyuan.constant.order;

public enum OrderItemGroupConstants {
	
	GROUP_ID (1, "包裹ID"),
	ORDER_ID (2, "订单ID");

	private  int intValue;
	private  String name;
	
	OrderItemGroupConstants(int intValue, String name) {
		this.intValue = intValue;
		this.name = name;
	}
	
	public int getIntValue() {
		return intValue;
	}
	
	public String getName() {
		return name;
	}
}
