package com.yujj.entity.community;



import java.io.Serializable;

import org.apache.commons.lang3.StringEscapeUtils;

public class UserQuestion implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private String content;

	private long createTime;
	
	private long id;
	
	private long yjjNumber;
	
	private long userId;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getYjjNumber() {
		return yjjNumber;
	}

	public void setYjjNumber(long yjjNumber) {
		this.yjjNumber = yjjNumber;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
	



	
}
