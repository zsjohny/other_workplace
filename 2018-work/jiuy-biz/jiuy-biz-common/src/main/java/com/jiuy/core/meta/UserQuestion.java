package com.jiuy.core.meta;

import java.io.Serializable;

import com.jiuyuan.entity.BaseMeta;

public class UserQuestion extends BaseMeta<Object> implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8323230471770652431L;

	private long id;
	
	private String content;
	
	private long yJJNumber;
	
	private long userId;
	
	private long createTime;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getyJJNumber() {
		return yJJNumber;
	}

	public void setyJJNumber(long yJJNumber) {
		this.yJJNumber = yJJNumber;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	@Override
	public Object getCacheId() {
		return null;
	}

}
