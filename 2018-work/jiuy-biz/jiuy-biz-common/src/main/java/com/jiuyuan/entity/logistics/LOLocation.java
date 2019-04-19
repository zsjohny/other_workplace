package com.jiuyuan.entity.logistics;

import java.io.Serializable;

public class LOLocation implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7476491739321751286L;
	
	private int id;
	
	private int type;
	
	private long createTime;
	
	private long updateTime;
	
	private int status;
	
	private String provinceName;
	
	private String cityName;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getProvinceName() {
		return provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	
	public String getLocation() {
		return getProvinceName() + " " + getCityName();
	}
	
}
