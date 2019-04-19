package com.jiuyuan.entity.storeorder;

import java.io.Serializable;

/**
* @author WuWanjian
* @version 创建时间: 2016年11月9日 下午6:04:46
*/
public class StoreOrderDiscountLog implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7270696371430828575L;

	private long id;
	
	private long orderNo;
	
	private long relatedId;
	
	private int type;	//0 品牌分类, 1虚拟分类, 2首单优惠, 3全场满减
	
	private double discount;
	
	private String comment;
	
	private long createTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
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
	
	
}
