package com.jiuy.core.meta.order;

/**
 * @author jeff.zhan
 * @version 2017年1月4日 上午10:12:02
 * 
 */

public class OrderNewSO {
	
	private String orderNo;
	
	private String receiver;
	
	private String expressNo;
	
	private String clothesNum;
	
	private Long yJJNumber;
	
	private Long skuNo;
	
	private String phone;
	
	private Long warehouseId;
	
	private String brandOrderNo;
	
	private Integer brandOrderStatus;
	
	private Long startTime;
	
	private Long endTime;
	
	private Long parentId;
	
	public OrderNewSO() {
		this.startTime = 0L;
		this.endTime = System.currentTimeMillis();
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

	public void setExpressNo(String expressNo) {
		this.expressNo = expressNo;
	}

	public String getClothesNum() {
		return clothesNum;
	}

	public void setClothesNum(String clothesNum) {
		this.clothesNum = clothesNum;
	}

	public Long getyJJNumber() {
		return yJJNumber;
	}

	public void setyJJNumber(Long yJJNumber) {
		this.yJJNumber = yJJNumber;
	}

	public Long getSkuNo() {
		return skuNo;
	}

	public void setSkuNo(Long skuNo) {
		this.skuNo = skuNo;
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

	public Long getStartTime() {
		return startTime;
	}

	public void setStartTime(Long startTime) {
		this.startTime = startTime;
	}

	public Long getEndTime() {
		return endTime;
	}

	public void setEndTime(Long endTime) {
		this.endTime = endTime;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}
	
	
}
