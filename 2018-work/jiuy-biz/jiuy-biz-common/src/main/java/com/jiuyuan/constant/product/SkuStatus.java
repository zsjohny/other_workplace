package com.jiuyuan.constant.product;

public enum SkuStatus {
	/**
	 * 全部
	 */
	ALL(0, "全部商品"),
	
	/**
	 * 全部
	 */
	NORMAL(1, "正常"),
	
	/**
	 * 全部
	 */
	STOCKOUT(2, "已售罄"),
	
	/**
	 * 缺货(库存量小于等于10个为缺货状态)
	 */
	LESS_GOODS(3, "缺货");
	
	private int intValue;
	
	private String desc;
	
	SkuStatus(int intValue, String desc) {
		this.intValue = intValue;
		this.desc = desc;
	}

	public int getIntValue() {
		return intValue;
	}

	public String getDesc() {
		return desc;
	}
}
