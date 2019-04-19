package com.jiuy.core.meta.brandorder;

import com.jiuyuan.constant.order.OrderStatus;

/**
 * @author jeff.zhan
 * @version 2016年12月26日 下午4:17:32
 * 
 */

public class BrandOrderSO {
	
	private OrderStatus orderStatus;
	
	private Long orderNo;
	
	private Long mergedId;
	
	private Integer relatedOrderType;
	
	private Long brandBusinessId;
	
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public Long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(Long orderNo) {
		this.orderNo = orderNo;
	}

	public Long getMergedId() {
		return mergedId;
	}

	public void setMergedId(Long mergedId) {
		this.mergedId = mergedId;
	}

	public Integer getRelatedOrderType() {
		return relatedOrderType;
	}

	public void setRelatedOrderType(Integer relatedOrderType) {
		this.relatedOrderType = relatedOrderType;
	}

	public Long getBrandBusinessId() {
		return brandBusinessId;
	}

	public void setBrandBusinessId(Long brandBusinessId) {
		this.brandBusinessId = brandBusinessId;
	}

	
}
