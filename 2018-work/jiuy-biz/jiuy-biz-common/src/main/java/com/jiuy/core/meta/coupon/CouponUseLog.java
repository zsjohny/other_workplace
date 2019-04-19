package com.jiuy.core.meta.coupon;

import java.io.Serializable;

import com.jiuyuan.constant.coupon.CouponUseStatus;

public class CouponUseLog implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8842160109799631918L;

	private Long id;
	
	private Long couponId;
	
	private Long userId;
	
	private Long orderNo;
	
	private Double actualDiscount;
	
	private CouponUseStatus status;
	
	private Long createTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCouponId() {
		return couponId;
	}

	public void setCouponId(Long couponId) {
		this.couponId = couponId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}

	public Double getActualDiscount() {
		return actualDiscount;
	}

	public void setActualDiscount(Double actualDiscount) {
		this.actualDiscount = actualDiscount;
	}

	public CouponUseStatus getStatus() {
		return status;
	}

	public void setStatus(CouponUseStatus status) {
		this.status = status;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}
	
	
}
