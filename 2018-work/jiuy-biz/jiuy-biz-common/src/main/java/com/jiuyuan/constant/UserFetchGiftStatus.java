package com.jiuyuan.constant;

public enum UserFetchGiftStatus {
	
	UNFETCHED("未领取"),
	
	FETCHED("已领取"),
	
	NO_GIFT("没有奖品");
	
	private String description;
	
	private UserFetchGiftStatus(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return description;
	}
}
