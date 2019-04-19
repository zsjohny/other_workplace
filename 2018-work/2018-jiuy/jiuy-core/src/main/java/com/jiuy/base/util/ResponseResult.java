package com.jiuy.base.util;

import com.jiuy.base.enums.GlobalsEnums;
import lombok.Data;

/**
 * 统一返回对象
 * @author Aison
 * @version V1.0
 * @Copyright 玖远网络
 * @date 2018/5/29 9:26
 */
@Data
public class ResponseResult {

	private Integer status;
	private String code;
	private String msg;
	private Object data;
	private boolean successful;
	private String error;
	private String html;

	public static ResponseResult SUCCESS = new ResponseResult().success();

	public static ResponseResult FAILED = new ResponseResult().failed();

	public static ResponseResult instance() {
		return new ResponseResult();
	}

	public ResponseResult() {

	}

	public ResponseResult(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public ResponseResult(GlobalsEnums ge) {
		this.code = ge.getCode().toString();
		this.msg = ge.getMsg();
		if (!this.code.equals("200")) {
			this.status = 0;
		} else {
			this.status = 1;
		}
	}

	/**
	 * status 0 为失败 1位成功
	 **/
	public ResponseResult(GlobalsEnums ge, Integer status) {
		this.code = ge.getCode().toString();
		this.msg = ge.getMsg();
		this.status = status;
	}

	public Integer getStatus() {
		return this.status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getCode() {
		return this.code;
	}

	public ResponseResult setCode(String code) {
		this.code = code;
		return this;
	}

	public String getMsg() {
		return this.msg;
	}

	public ResponseResult setMsg(String msg) {
		this.msg = msg;
		return this;
	}

	public Object getData() {
		return this.data;
	}

	public ResponseResult setData(Object data) {
		this.data = data;
		// this.data = data==null?"":data;
			// this.data = BIZ.NullToEmpty(data);
		return this;
	}

	public ResponseResult success() {
		this.code = GlobalsEnums.SUCCESS.getCode().toString();
		this.msg = GlobalsEnums.SUCCESS.getMsg();
		this.status = 1;
		this.data = Biz.NullToEmpty(data);
		this.successful = true;
		return this;
	}

	public ResponseResult success(Object data) {
		this.code = GlobalsEnums.SUCCESS.getCode().toString();
		this.status = 1;
		this.msg = GlobalsEnums.SUCCESS.getMsg();
		this.successful = true;
		setData(data);
		return this;
	}

	public ResponseResult success(String msg) {
		this.code = GlobalsEnums.SUCCESS.getCode().toString();
		this.msg = msg;
		this.status = 1;
		this.data = Biz.NullToEmpty(data);
		this.successful = true;
		return this;
	}

	public ResponseResult success(String msg, Object data) {
		this.code = GlobalsEnums.SUCCESS.getCode().toString();
		this.msg = msg;
		setData(data);
		this.status = 1;
		this.successful = true;
		return this;
	}
	
	public ResponseResult failed() {
		this.code = GlobalsEnums.FAILED.getCode().toString();
		this.msg = GlobalsEnums.FAILED.getMsg();
		this.status = 0;
		this.successful = false;
		return this;
	}

	public ResponseResult failed(String msg) {
		this.code = GlobalsEnums.FAILED.getCode().toString();
		this.msg = msg;
		this.status = 0;
		this.successful = false;
		return this;
	}

	public ResponseResult statusFaile() {

		this.status = 0;
		return this;
	}

	public ResponseResult statusSuccess() {

		this.status = 1;
		return this;
	}

}
