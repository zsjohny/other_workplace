package com.jiuyuan.entity;

import java.io.Serializable;

import com.jiuyuan.constant.order.OrderStatus;

public class OrderNewLog implements Serializable {

    private static final long serialVersionUID = -8701127740259286937L;
    
    private long Id;
    
    private long storeId;
    
    private long orderNo;
    
    private int oldStatus;
    
    private int newStatus;
    
    private long createTime;
    
    private long userId;

   

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

   


    public long getId() {
        return Id;
    }

    public void setId(long Id) {
        this.Id = Id;
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

	@Override
	public String toString() {
		return "OrderNewLog [Id=" + Id + ", storeId=" + storeId + ", orderNo=" + orderNo + ", oldStatus=" + oldStatus
				+ ", newStatus=" + newStatus + ", createTime=" + createTime + "]";
	}

}
