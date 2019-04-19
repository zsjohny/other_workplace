package com.jiuyuan.entity;

import java.io.Serializable;

public class StoreFinanceLog extends BaseMeta<Long> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2609495993856840287L;
	
	private long id;
	
	private long storeId;
	
	private int type;
	
	private long relatedId;
	
	private double cash;
	
	private long createTime;
	
	private long userId;
	
	private long updateTime;
	
	public StoreFinanceLog() {
		
	}

	public StoreFinanceLog(long userId,long belongStoreId, int i,  double returnMoney, long currentTimeMillis,long serviceId) {
		this.userId=userId;
		this.storeId =belongStoreId;
		this.type=i;
		this.cash=returnMoney;
		this.createTime=currentTimeMillis;
		this.relatedId=serviceId;
	}

	public StoreFinanceLog(long id, long relatedId, int type, long currentTimeMillis) {
		this.relatedId = id;
		this.storeId = relatedId;
		this.type = type;
		this.updateTime = currentTimeMillis;
	}

	public StoreFinanceLog(long id, long relatedId, int type, double money, long currentTimeMillis) {
		this.relatedId = id;
		this.storeId = relatedId;
		this.type = type;
		this.cash = money;
		this.createTime = currentTimeMillis;
	}

	public long getUpdateTime() {
		return updateTime;
	}



	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
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



	public double getCash() {
		return cash;
	}



	public void setCash(double cash) {
		this.cash = cash;
	}



	public long getCreateTime() {
		return createTime;
	}



	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}


	public long getUserId() {
		return userId;
	}



	public void setUserId(long userId) {
		this.userId = userId;
	}



	@Override
	public Long getCacheId() {
		return null;
	}
	

}
