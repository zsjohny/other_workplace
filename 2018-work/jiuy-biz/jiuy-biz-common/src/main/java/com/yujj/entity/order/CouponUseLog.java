package com.yujj.entity.order;

import java.io.Serializable;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;

public class CouponUseLog implements Serializable{

	public CouponUseLog() {
    }
	
	private static final long serialVersionUID = 272646017895697773L;

	private long id;
	
	private long couponId;
	
	private long userId;
	
	private long orderNo;
	
    private int status;
    
	private long createTime;
	
	private double actualDiscount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getCouponId() {
		return couponId;
	}

	public void setCouponId(long couponId) {
		this.couponId = couponId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
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

	public double getActualDiscount() {
		return actualDiscount;
	}

	public void setActualDiscount(double actualDiscount) {
		this.actualDiscount = actualDiscount;
	}
	
	
	
	
}
