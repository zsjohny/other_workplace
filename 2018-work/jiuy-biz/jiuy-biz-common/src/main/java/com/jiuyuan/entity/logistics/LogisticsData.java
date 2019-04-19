package com.jiuyuan.entity.logistics;

import java.io.Serializable;
import java.util.Date;

public class LogisticsData implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Date time;
    private String context;
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getContext() {
		return context;
	}
	public void setContext(String context) {
		this.context = context;
	}
    
}
