package com.yujj.web.controller.wap.pay2;

public class CgUser {
	private String wechatId;
	private String name;


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWechatId() {
		return wechatId;
	}

	public void setWechatId(String wechatId) {
		this.wechatId = wechatId;
	}

	@Override
	public String toString() {
		return "CgUser [wechatId=" + wechatId + "]";
	}
	
	
}
