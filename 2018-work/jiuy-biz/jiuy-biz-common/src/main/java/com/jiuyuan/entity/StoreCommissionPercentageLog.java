package com.jiuyuan.entity;

import java.io.Serializable;

public class StoreCommissionPercentageLog implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7994920143877988871L;

	private long id;
	
	private long storeId;
	
	private int type;
	
	private long relatedId;
	
	private double oldCommissionPercentage;
	
	private double newCommissionPercentage;
	
	private long createTime;
	

	public StoreCommissionPercentageLog() {
	}

	public StoreCommissionPercentageLog(long storeId, int type, long relatedId, double oldPercentage, double newPercentage,
			long time) {
		this.storeId = storeId;
		this.type = type;
		this.relatedId = relatedId;
		this.oldCommissionPercentage = oldPercentage;
		this.newCommissionPercentage = newPercentage;
		this.createTime = time;
	}

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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getRelatedId() {
		return relatedId;
	}

	public void setRelatedId(long relatedId) {
		this.relatedId = relatedId;
	}

	public double getOldCommissionPercentage() {
		return oldCommissionPercentage;
	}

	public void setOldCommissionPercentage(double oldCommissionPercentage) {
		this.oldCommissionPercentage = oldCommissionPercentage;
	}

	public double getNewCommissionPercentage() {
		return newCommissionPercentage;
	}

	public void setNewCommissionPercentage(double newCommissionPercentage) {
		this.newCommissionPercentage = newCommissionPercentage;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
}