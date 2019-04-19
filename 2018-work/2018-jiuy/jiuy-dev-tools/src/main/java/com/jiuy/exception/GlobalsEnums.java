package com.jiuy.exception;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public enum GlobalsEnums {
	
	DRIVER_LOAD_ERROR(1, "驱动加载失败"),
	CONNECTION_GET_ERROR(2,"获取数据库连接失败"),
	COMMENTS_LOAD_ERROR(3,"获取列的注释的时候出现错误"),
	PK_GET_ERROR(4,"获取主键失败"),
	COMMENT_INVALID(5,"注释不合法,请确认是否有注释，注释格式 主键:, 主键:sf"),
	TABLE_COMMENT_INVALID(6,"表注释不合法,请确认是否有注释"),
	AUTHOR_ERROR(6,"请注明作者谢谢 <author>aichengsong</author>");
	
	
	
	private Integer code;

	private String msg;

	private GlobalsEnums(int code) {
		this.code = code;
	}

	private GlobalsEnums(int code, String msg) {
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
