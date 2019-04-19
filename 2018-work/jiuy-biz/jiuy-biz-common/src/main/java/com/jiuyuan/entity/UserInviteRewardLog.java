package com.jiuyuan.entity;

import java.io.Serializable;

public class UserInviteRewardLog implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 456776677728099151L;

	private Long id;
	
	private Long userId;
	
	private Long couponTemplateId;
	
	private Integer count;
	
	private Integer jiuCoin;
	
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

	public Long getCouponTemplateId() {
		return couponTemplateId;
	}

	public void setCouponTemplateId(Long couponTemplateId) {
		this.couponTemplateId = couponTemplateId;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getJiuCoin() {
		return jiuCoin;
	}

	public void setJiuCoin(Integer jiuCoin) {
		this.jiuCoin = jiuCoin;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	
	
}
