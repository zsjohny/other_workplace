package com.store.entity;

import java.io.Serializable;

public class StoreCartItem implements Serializable {

    private static final long serialVersionUID = 2472768480252286671L;

    private long id;

    private long storeId;

    private long productId;

    private long skuId;

    private int buyCount;

    private int status;

    private long createTime;

    private long updateTime;
    
    private int isSelected;
    
    private long statisticsId;
    
    public StoreCartItem() {
		super();
	}

	public StoreCartItem(long storeId, long productId, long skuId, int buyCount, int status, long createTime,
			long updateTime, int isSelected, long statisticsId) {
		super();
		this.storeId = storeId;
		this.productId = productId;
		this.skuId = skuId;
		this.buyCount = buyCount;
		this.status = status;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.isSelected = isSelected;
		this.statisticsId = statisticsId;
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

    public int getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(int buyCount) {
        this.buyCount = buyCount;
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

	public int getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(int isSelected) {
		this.isSelected = isSelected;
	}

	public long getStatisticsId() {
		return statisticsId;
	}

	public void setStatisticsId(long statisticsId) {
		this.statisticsId = statisticsId;
	}



}
