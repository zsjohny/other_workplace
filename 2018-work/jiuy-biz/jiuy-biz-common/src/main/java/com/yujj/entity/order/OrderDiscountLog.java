package com.yujj.entity.order;

import java.io.Serializable;

public class OrderDiscountLog implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1136084348591457323L;

	private long id;
	
//	private long orderId;
	
	private long relatedId;
	
	private int type;
	
	private double discount;
	
	private String comment;
	
	private long createTime;
	
	private long orderNo;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
//	public long getOrderId() {
//		return orderId;
//	}
//	
//	public void setOrderId(long orderId) {
//		this.orderId = orderId;
//	}
	
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
	
	public double getDiscount() {
		return discount;
	}
	
	public void setDiscount(double discount) {
		this.discount = discount;
	}
	
	public String getComment() {
		return comment;
	}
	
	public void setComment(String comment) {
		this.comment = comment;
	}
	
	public long getCreateTime() {
		return createTime;
	}
	
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}
}