package com.jiuyuan.entity.storeorder;

import java.io.Serializable;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月9日 下午6:09:25
*/
public class StoreOrderLog1 implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1814240886040359199L;

	private long id;
	
	private long storeId;
	
	private long orderNo;
	
	private int oldStatus;
	
	private int newStatus;
	
	private long createTime;
	
	private long updateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getStoreId() {
		return storeId;
	}

	public void setStoreId(long storeId) {
		this.storeId = storeId;
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

	@Override
	public String toString() {
		return "StoreOrderLog1 [id=" + id + ", storeId=" + storeId + ", orderNo=" + orderNo + ", oldStatus=" + oldStatus
				+ ", newStatus=" + newStatus + ", createTime=" + createTime + ", updateTime=" + updateTime + "]";
	}
	
	
}
