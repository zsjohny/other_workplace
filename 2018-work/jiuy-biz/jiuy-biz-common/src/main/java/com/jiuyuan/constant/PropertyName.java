package com.jiuyuan.constant;

public enum PropertyName {
	
	STYLE (4, "风格"),
	
	ELEMENT (5, "元素"),
	
	BRAND (6, "品牌"),
	
	COLOR (7, "颜色"),
	
	SIZE (8, "尺码"),
	
	SEASON (9, "季节"),
	
	YEAR (10, "年份");

	private long intValue;
	
	private String name;
	
	PropertyName(int intValue, String name) {
		this.intValue = intValue;
		this.name = name;
	}
	
	public long getValue() {
		return intValue;
	}
	
	public String getName() {
		return name;
	}
	
	public static PropertyName getByIntValue(long intValue) {
		for (PropertyName propertyName : PropertyName.values()) {
			if (propertyName.getValue() == intValue) {
				return propertyName;
			}
		}
		
		return null;
	}
	
}
