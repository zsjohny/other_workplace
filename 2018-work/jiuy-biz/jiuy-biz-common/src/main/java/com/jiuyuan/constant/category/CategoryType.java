package com.jiuyuan.constant.category;

public enum CategoryType {
	NORMAL(0, "正常产品分类"),
	
	PARTNER(1, "商家分类"),
	
	VIRTUAL(2, "虚拟分类");
	
	private int intValue;
	private String name;

	CategoryType(int intValue, String name) {
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
