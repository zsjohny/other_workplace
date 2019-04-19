package com.jiuy.base.exception;

import com.jiuy.base.enums.GlobalsEnums;

/**
 * 数据库操作异常 不要手动去抛出次异常
 **/
public class DBException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private int code;

	private String msg;

	private String nameSpace;

	public DBException(GlobalsEnums db, String nameSpace) {
		this.code = db.getCode();
		this.msg = db.getMsg();
		this.nameSpace = nameSpace;
	}

	public DBException(int code, String msg, String nameSpace) {
		this.code = code;
		this.msg = msg;
		this.nameSpace = nameSpace;
	}

	public DBException() {

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

	public String getNameSpace() {
		return nameSpace;
	}

	public void setNameSpace(String nameSpace) {
		this.nameSpace = nameSpace;
	}

}
