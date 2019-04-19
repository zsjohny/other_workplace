package com.jiuyuan.util;


@SuppressWarnings("serial")
public class TipsMessageException extends RuntimeException{
	//提示语
	private String friendlyMsg;
	
	public TipsMessageException(String friendlyMsg){
		this.friendlyMsg = friendlyMsg;
	}

	public String getFriendlyMsg() {
		return friendlyMsg;
	}

	public void setFriendlyMsg(String friendlyMsg) {
		this.friendlyMsg = friendlyMsg;
	}




}
