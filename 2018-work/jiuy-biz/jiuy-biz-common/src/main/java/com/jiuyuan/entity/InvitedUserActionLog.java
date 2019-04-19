package com.jiuyuan.entity;

import java.io.Serializable;

public class InvitedUserActionLog implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 578422460349003286L;

	private Long id;
	
	private Long userId;
	
	private Long invitor;
	
	private Integer action;
	
	private Long relatedId;
	
	private Long createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getInvitor() {
		return invitor;
	}

	public void setInvitor(Long invitor) {
		this.invitor = invitor;
	}

	public Integer getAction() {
		return action;
	}

	public void setAction(Integer action) {
		this.action = action;
	}

	public Long getRelatedId() {
		return relatedId;
	}

	public void setRelatedId(Long relatedId) {
		this.relatedId = relatedId;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	
	
}
