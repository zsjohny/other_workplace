package com.jiuyuan.constant.category;

public enum CategoryStatus {
	
	/**
	 * 删除
	 */
	REMOVED(-1),
	
	/**
	 * 正常
	 */
	NORMAL(0),
	
	/**
	 * 隐藏
	 */
	HIDE(1);
	
	CategoryStatus(int intValue) {
		this.intValue = intValue;
	}
	
	private int intValue;
	
	public int getIntValue() {
		return this.intValue;
	}
}
