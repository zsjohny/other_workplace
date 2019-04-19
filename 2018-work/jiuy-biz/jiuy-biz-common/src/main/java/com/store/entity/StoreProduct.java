package com.store.entity;

import java.io.Serializable;

/**
 * @author jeff.zhan
 * @version 2016年11月30日 下午5:42:36
 * 
 */

public class StoreProduct implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5440365147351753279L;

	private long id;
	
	private long storeId;
	
	private long productId;
	
	private long skuId;
	
	private int onSaleCount;
	
	private int offSaleCount;
	
	private int status;
	
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

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public long getSkuId() {
		return skuId;
	}

	public void setSkuId(long skuId) {
		this.skuId = skuId;
	}

	public int getOnSaleCount() {
		return onSaleCount;
	}

	public void setOnSaleCount(int onSaleCount) {
		this.onSaleCount = onSaleCount;
	}

	public int getOffSaleCount() {
		return offSaleCount;
	}

	public void setOffSaleCount(int offSaleCount) {
		this.offSaleCount = offSaleCount;
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
