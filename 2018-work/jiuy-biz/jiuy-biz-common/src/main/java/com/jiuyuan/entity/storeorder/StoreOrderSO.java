package com.jiuyuan.entity.storeorder;

import com.jiuyuan.constant.order.OrderStatus;

/**
 * @author jeff.zhan
 * @version 2016年12月26日 上午11:53:58
 * 
 */

public class StoreOrderSO {
	
	private OrderStatus orderStatus;
	
	private String orderNo;
	
	private String receiver;
	
	private String expressNo;
	
	private String clothesNum;
	
	private Long businessNumber;
	
	private Long skuNo;
	
	private String phone;
	
	private Long warehouseId;
	
	private String brandOrderNo;
	
	private Integer brandOrderStatus;
	
	private long startTime;
	
	private long endTime;
	
	private Long parentId;
	
	public StoreOrderSO() {
		super();
	}

	public StoreOrderSO(String orderNo, String receiver, String expressNo, String clothesNum,
			Long businessNumber, Long skuNo, String phone, Long warehouseId, String brandOrderNo,
			Integer brandOrderStatus, long startTime, long endtime) {
		super();
		this.orderNo = orderNo;
		this.receiver = receiver;
		this.expressNo = expressNo;
		this.clothesNum = clothesNum;
		this.businessNumber = businessNumber;
		this.skuNo = skuNo;
		this.phone = phone;
		this.warehouseId = warehouseId;
		this.brandOrderNo = brandOrderNo;
		this.brandOrderStatus = brandOrderStatus;
		this.startTime = startTime;
		this.endTime = endtime;
	}
	
	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getExpressNo() {
		return expressNo;
	}

	public void setExpressOrderNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public String getClothesNum() {
		return clothesNum;
	}

	public void setClothesNum(String clothesNum) {
		this.clothesNum = clothesNum;
	}

	public Long getBusinessNumber() {
		return businessNumber;
	}

	public void setBusinessNumber(Long businessNumber) {
		this.businessNumber = businessNumber;
	}

	public Long getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(Long skuNo) {
		this.skuNo = skuNo;
	}

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getBrandOrderNo() {
		return brandOrderNo;
	}

	public void setBrandOrderNo(String brandOrderNo) {
		this.brandOrderNo = brandOrderNo;
	}

	public Integer getBrandOrderStatus() {
		return brandOrderStatus;
	}

	public void setBrandOrderStatus(Integer brandOrderStatus) {
		this.brandOrderStatus = brandOrderStatus;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

}
