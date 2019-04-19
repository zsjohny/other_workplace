package com.jiuy.core.meta.brandorder;

import java.io.Serializable;

/**
 * @author jeff.zhan
 * @version 2017年1月3日 下午2:25:01
 * 
 */

public class BrandOrderLog implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7631745412138447479L;

	private long id;
	
	private long brandBusinessId;
	
	private long orderNo;
	
	private int oldStatus;
	
	private int newStatus;
	
	private long createTime;
	
	private long updateTime;
	
	public BrandOrderLog() {
	}
	
	public BrandOrderLog(long brandBusinessId, long orderNo, int oldStatus, int newStatus, long createTime,
			long updateTime) {
		super();
		this.brandBusinessId = brandBusinessId;
		this.orderNo = orderNo;
		this.oldStatus = oldStatus;
		this.newStatus = newStatus;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getBrandBusinessId() {
		return brandBusinessId;
	}

	public void setBrandBusinessId(long brandBusinessId) {
		this.brandBusinessId = brandBusinessId;
	}

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

	public int getOldStatus() {
		return oldStatus;
	}

	public void setOldStatus(int oldStatus) {
		this.oldStatus = oldStatus;
	}

	public int getNewStatus() {
		return newStatus;
	}

	public void setNewStatus(int newStatus) {
		this.newStatus = newStatus;
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
	
	
}
