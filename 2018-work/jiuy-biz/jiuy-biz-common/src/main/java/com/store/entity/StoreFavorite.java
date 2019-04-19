package com.store.entity;

import java.io.Serializable;

/**
 * @author jeff.zhan
 * @version 2016年12月10日 下午2:40:20
 * 
 */

public class StoreFavorite implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3936780633370866997L;

	private long id;

	private long storeId;

	private long relatedId;

	private int type;

	private int status;

	private long updateTime;

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

	public long getRelatedId() {
		return relatedId;
	}

	public void setRelatedId(long relatedId) {
		this.relatedId = relatedId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	
	
}
