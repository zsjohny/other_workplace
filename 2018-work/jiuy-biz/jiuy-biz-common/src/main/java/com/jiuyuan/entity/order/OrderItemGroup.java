package com.jiuyuan.entity.order;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiuyuan.constant.order.OrderStatus;

public class OrderItemGroup implements Serializable {

    private static final long serialVersionUID = -1325734073742439806L;

    private long id;

    private long orderId;

    private long userId;

    private long brandId;

    @JsonIgnore
    private int status;

    @JsonIgnore
    private long createTime;

    @JsonIgnore
    private long updateTime;

    private double totalMoney;

    private double totalExpressMoney;
    
    private int totalUnavalCoinUsed;

//    private OrderStatus orderStatus;
    
    private long lOWarehouseId;
    
    private double totalPay;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getBrandId() {
        return brandId;
    }

    public void setBrandId(long brandId) {
        this.brandId = brandId;
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

    public double getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(double totalMoney) {
        this.totalMoney = totalMoney;
    }

    public double getTotalExpressMoney() {
        return totalExpressMoney;
    }

    public void setTotalExpressMoney(double totalExpressMoney) {
        this.totalExpressMoney = totalExpressMoney;
    }

//    public OrderStatus getOrderStatus() {
//        return orderStatus;
//    }
//
//    public void setOrderStatus(OrderStatus orderStatus) {
//        this.orderStatus = orderStatus;
//    }

	public int getTotalUnavalCoinUsed() {
		return totalUnavalCoinUsed;
	}

	public void setTotalUnavalCoinUsed(int totalUnavalCoinUsed) {
		this.totalUnavalCoinUsed = totalUnavalCoinUsed;
	}

	public long getlOWarehouseId() {
		return lOWarehouseId;
	}

	public void setlOWarehouseId(long lOWarehouseId) {
		this.lOWarehouseId = lOWarehouseId;
	}

	public double getTotalPay() {
		return totalPay;
	}

	public void setTotalPay(double totalPay) {
		this.totalPay = totalPay;
	}
	
	
	
}
