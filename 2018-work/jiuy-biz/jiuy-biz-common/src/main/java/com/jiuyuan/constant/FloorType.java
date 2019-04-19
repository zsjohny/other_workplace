package com.jiuyuan.constant;

import org.apache.commons.lang3.StringUtils;

public enum FloorType {
	
	/**
	 * 活动专场(包含首页设置)
	 */
	ACTIVITY_PLACE(0, "activityPlace"),
	
	/**
	 * 分类
	 */
	CATEGORY(1, "category"),

	/**
	 * 积分商城
	 */
	POINT_MALL(2, "pointMall"),
	
	SHOP_INDEX(3, "shopIndex");

	private int typeValue;
	
	private String stringValue;
	
	FloorType(int typeValue, String stringValue) {
		this.typeValue = typeValue;
		this.stringValue = stringValue;
	}

	public int getTypeValue() {
		return typeValue;
	}

	public String getStringValue() {
		return stringValue;
	}
	
	public static FloorType getByStringValue(String stringValue) {
		for (FloorType fType : FloorType.values()) {
			if (StringUtils.equals(fType.getStringValue(), stringValue)) {
				return fType;
			}
		}
		return null;
	}
}
