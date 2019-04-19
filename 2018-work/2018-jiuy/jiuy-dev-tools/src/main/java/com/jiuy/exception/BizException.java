package com.jiuy.exception;

/**
 * 业务逻辑异常
 **/
public class BizException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private int code;

	private String msg;

	private String keyVal;

	public String getKeyVal() {
		return keyVal;
	}

	public void setKeyVal(String keyVal) {
		this.keyVal = keyVal;
	}

	private GlobalsEnums ge;

	public GlobalsEnums getGe() {
		return ge;
	}

	public void setGe(GlobalsEnums ge) {
		this.ge = ge;
	}
	
	public BizException(GlobalsEnums ge) {
		this.code = ge.getCode();
		this.msg = ge.getMsg();
		this.ge = ge;
	}

	public BizException(GlobalsEnums ge,String custom) {
		custom = custom == null ? "" : custom;
		this.code = ge.getCode();
		this.msg = ge.getMsg() +" \r\n "+custom;
		this.ge = ge;
	}

	public BizException(String keyVal) {
		this.keyVal = keyVal;
	}

	public BizException(int code, String msg) {
		this.code = code;
		this.msg = msg;
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
