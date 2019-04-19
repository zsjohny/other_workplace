/**
 * 
 */
package com.jiuy.web.controller.util.model;

import javax.servlet.http.HttpServletRequest;

import com.jiuyuan.constant.ResultCode;

/**
 * @author LWS
 *
 * 通用应答消息模型类
 */
public class CommonResponseObject<T extends Object> {
	
	public CommonResponseObject(){
		
	}
	
	public CommonResponseObject(int code,String errorMsg,T result){
		this.code = code;
		this.errorMsg = errorMsg;
		this.result = result;
	}

	private int code = 0;
	private String errorMsg;
	private T result;
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
	
	public void setResult(ResultCode resultCode,T result){
	    this.code = resultCode.getCode();
	    this.errorMsg = resultCode.getDesc();
	    this.result = result;
	}
	
	public static String getRemortIP(HttpServletRequest request) {
	  	  if (request.getHeader("x-forwarded-for") == null) {
	  	   return request.getRemoteAddr();
	  	  }
	  	  return request.getHeader("x-forwarded-for");
	  	}
}
