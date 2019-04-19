package com.jiuyuan.entity;

import java.io.Serializable;

import org.apache.commons.lang3.StringUtils;

import com.jiuyuan.entity.BaseMeta;

public class ExpressInfo extends BaseMeta<Long> implements Serializable {

    private static final long serialVersionUID = -5580468014310810478L;
    
    private long id;
    
    private long orderNo;
    private long userId;

    private long orderId;

    private long orderItemGroupId;
    
    
    private String expressSupplier;
    
    private String expressOrderNo;
    
    private long expressUpdateTime;
    
    private long createTime;
    
    private long updateTime;
    
    private int status;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }



    
    public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getOrderId() {
		return orderId;
	}

	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}

	public long getOrderItemGroupId() {
		return orderItemGroupId;
	}

	public void setOrderItemGroupId(long orderItemGroupId) {
		this.orderItemGroupId = orderItemGroupId;
	}

	public String getExpressSupplier() {
        return StringUtils.lowerCase(expressSupplier);
    }
    public void setExpressSupplier(String expressSupplier) {
        this.expressSupplier = expressSupplier;
    }

    public String getExpressOrderNo() {
        return expressOrderNo;
    }

    public void setExpressOrderNo(String expressOrderNo) {
        this.expressOrderNo = expressOrderNo;
    }

    public long getExpressUpdateTime() {
        return expressUpdateTime;
    }

    public void setExpressUpdateTime(long expressUpdateTime) {
        this.expressUpdateTime = expressUpdateTime;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

	@Override
	public Long getCacheId() {
        return null;
    }
}
