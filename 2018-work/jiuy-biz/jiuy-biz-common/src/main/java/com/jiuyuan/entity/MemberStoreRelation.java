package com.jiuyuan.entity;

import java.io.Serializable;

/**
 * @author jeff.zhan
 * @version 2016年10月26日 下午4:36:56
 * 
 */
public class MemberStoreRelation implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1004945802815102419L;

	private long id;
	
	private long memberUserId;
	
	private long businessId;
	
	private long createTime;
	
	private int status;
	
	private int type;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getMemberUserId() {
		return memberUserId;
	}

	public void setMemberUserId(long memberUserId) {
		this.memberUserId = memberUserId;
	}

	public long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(long businessId) {
		this.businessId = businessId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	
}
