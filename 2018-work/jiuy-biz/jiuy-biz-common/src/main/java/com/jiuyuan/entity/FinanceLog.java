package com.jiuyuan.entity;

import java.io.Serializable;

/**
 * @author jeff.zhan
 * @version 2016年11月17日 上午9:40:17
 * 
 */

public class FinanceLog implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3601354094802395621L;

	private long id;
	
	private long storeId;
	
	private long userId;
	
	private int type;
	
	private long relatedId;
	
	private double cash;
	
	private long createTime;

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
	
	
}
