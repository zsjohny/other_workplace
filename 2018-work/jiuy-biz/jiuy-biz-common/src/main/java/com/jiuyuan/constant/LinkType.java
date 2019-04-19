package com.jiuyuan.constant;

public enum LinkType {
	
	NONE(-1, "无"),
	
	UNKNOWN(0, "未选择"),
	
	URL(1, "URL"),
	
	ARTICAL(2, "文章"),
	
	PRODUCT(3, "商品"),
	
	CATEGORY(4, "商品分类"),
		    
	COUPON(5, "代金券"),
	
	ARTICAL_CATEGORY(6, "文章分类"),
	
	DESIGNATE_COUPON(11, "指定用户代金券");
	
	private int intValue;
	
	private String description;
	
	private LinkType(int intValue, String description) {
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
