package com.jiuyuan.entity;

import java.io.Serializable;

public class UserSharedRecordOrderNew implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -600822122254793491L;
	private long id;
	private long orderNo;
	private long userSharedRecordId;
	private long createTime;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}
	public long getUserSharedRecordId() {
		return userSharedRecordId;
	}
	public void setUserSharedRecordId(long userSharedRecordId) {
		this.userSharedRecordId = userSharedRecordId;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
}
