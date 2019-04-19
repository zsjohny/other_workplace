package com.jiuyuan.entity;

import java.io.Serializable;

/**
 * @author jeff.zhan
 * @version 2016年11月4日 下午4:55:10
 * 
 */

public class ActivityPlace implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7408556501787999731L;

	private long id;
	
	private String name;
	
	private String description;
	
	private String url;
	
	/**
	 * 状态：0：正常；-1：删除
	 */
	private int status;
	
	private long createTime;
	
	private long updateTime;
	
	/**
	 * 专场类型：0：系统专场；1：自定义专场；2回收站
	 */
	private int type;

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
}