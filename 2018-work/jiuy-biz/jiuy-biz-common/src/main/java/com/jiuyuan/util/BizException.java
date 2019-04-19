package com.jiuyuan.util;


/**
 * 业务逻辑异常
 **/
@Deprecated
public class BizException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private int code;

	private String msg;

	private String keyVal;

	public String getKeyVal() {
		return keyVal;
	}

	public BizException(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}


	public static BizException defulat() {
		return new BizException(400,"");
	}
	public static BizException me(String msg) {
		return new BizException(400,msg);
	}
	public static BizException defulat(Integer code) {
		return new BizException(code,"");
	}

	public BizException paramError() {
		this.msg = "参数错误";
		return this;
	}
	
	public BizException msg(String msg) {
		this.msg = msg;
		return this;
	}
	
	public BizException() {

	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
