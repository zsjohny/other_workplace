package com.jiuy.base.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public enum GlobalsEnums {
	
	SYSTEM_ERROR(1, "服务暂不可用"), 
	SUCCESS(200, "成功"), 
	FAILED(400, "失败"), 
	NO_MODIFY(412, "没有被修改的内容"),
	PARAM_ERROR(455,"参数错误"),
	DATA_IS_CHANGED(1000, "数据已经被修改"),
	FILE_TOO_BIG(2000,"文件太大了"),
	FILE_UPLOAD_FAILED(2001,"文件上传失败"),

	LOG_OUT(3001,"用户已登出");

	private Integer code;

	private String msg;

	GlobalsEnums(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public static Map<Integer, String> map = new HashMap<Integer, String>();

	static {
		EnumSet<GlobalsEnums> set = EnumSet.allOf(GlobalsEnums.class);
		for (GlobalsEnums s : set) {
			map.put(s.getCode(), s.getMsg());
		}
	}


}
