package com.jiuyuan.util;

import com.jiuyuan.constant.ResultCode;

/**
 * @Description 业务异常的封装
 */
public class ResultCodeException extends RuntimeException {

	//友好提示的code码
	protected int friendlyCode;

	//友好提示
	protected String friendlyMsg;

	//业务异常跳转的页面  
	protected String urlPath;
	
	public ResultCodeException(ResultCode resultCode) {
		int friendlyCode = resultCode.getIntValue();
		String friendlyMsg = resultCode.getDesc();
		String urlPath = "";
		this.setValues(friendlyCode, friendlyMsg, urlPath);
	}
	public ResultCodeException(int friendlyCode, String friendlyMsg, String urlPath) {
		this.setValues(friendlyCode, friendlyMsg, urlPath);
	}

	public ResultCodeException(ResultCodeException bizExceptionEnum) {
		this.setValues(bizExceptionEnum.getCode(), bizExceptionEnum.getMessage(), bizExceptionEnum.getUrlPath());
	}

	private void setValues(int friendlyCode, String friendlyMsg, String urlPath) {
		this.friendlyCode = friendlyCode;
		this.friendlyMsg = friendlyMsg;
		this.urlPath = urlPath;
	}

	public int getCode() {
		return friendlyCode;
	}

	public void setCode(int code) {
		this.friendlyCode = code;
	}

	public String getMessage() {
		return friendlyMsg;
	}

	public void setMessage(String message) {
		this.friendlyMsg = message;
	}

	public String getUrlPath() {
		return urlPath;
	}

	public void setUrlPath(String urlPath) {
		this.urlPath = urlPath;
	}
	
}
