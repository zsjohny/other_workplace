package com.jiuyuan.entity;

import java.io.Serializable;

public class SearchMatchObject implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8975477014809822917L;

	private long id;
	
	private String name;
	
	private int status;
	
	private String matchName;
	
	private long createTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getMatchName() {
		return matchName;
	}

	public void setMatchName(String matchName) {
		this.matchName = matchName;
	}
	
}
