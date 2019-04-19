package com.jiuyuan.entity.logistics;

import java.io.Serializable;

public class LOPostage implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1199917006353964753L;
	
	private int id;
	
	private int deliveryLocation;
	
	private int distributionLocation;
	
	private double postage;
	
	private int status;
	
	private long createTime;
	
	private long updateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getDeliveryLocation() {
		return deliveryLocation;
	}

	public void setDeliveryLocation(int deliveryLocation) {
		this.deliveryLocation = deliveryLocation;
	}

	public int getDistributionLocation() {
		return distributionLocation;
	}

	public void setDistributionLocation(int distributionLocation) {
		this.distributionLocation = distributionLocation;
	}

	public double getPostage() {
		return postage;
	}

	public void setPostage(double postage) {
		this.postage = postage;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
