package com.jiuy.base.exception;

import com.jiuy.base.enums.GlobalsEnums;
import lombok.Data;


/**
 * 异常相关的类
 * @author Aison
 * @version V1.0
 * @Copyright 玖远网络
 * @date 2018/6/11 10:44
 */
@Data
public class BizException extends RuntimeException {


	private static final long serialVersionUID = 1L;

	private int code;

	private String msg;

	private String keyVal;

	public static BizException def() {
		return new BizException(400,"");
	}

	public static BizException paramError() {
		return new BizException(411,"");
	}

	public static BizException instance(GlobalsEnums ge) {
		return new BizException(ge);
	}

	public BizException msg (String msg) {
		this.msg = msg;
		return this;
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

	public BizException(String keyVal) {
		this.keyVal = keyVal;
	}

	public BizException(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public BizException() {

	}

}
