package com.jiuyuan.entity;

import java.io.Serializable;

public class FetchCouponCenterLog implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8627653964832544955L;

	private long id;
	
	private long couponTemplateId;
	
	private long userId;
	
	private long createTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCouponTemplateId() {
		return couponTemplateId;
	}

	public void setCouponTemplateId(long couponTemplateId) {
		this.couponTemplateId = couponTemplateId;
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
	
	
}
