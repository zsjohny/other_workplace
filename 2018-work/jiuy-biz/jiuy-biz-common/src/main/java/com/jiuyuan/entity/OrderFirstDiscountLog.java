package com.jiuyuan.entity;
/**
* @author WuWanjian
* @version 创建时间: 2017年4月10日 上午10:39:01
*/
public class OrderFirstDiscountLog {

	private long orderNo;
	
	private double totalMoney;
	
	private double totalPay;
	
	private double discount;
	
	private long userId;
	
	private long createTime;
	
	private String YJJNumber;
	
	private String createTimeStr;

	public long getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

	public double getTotalMoney() {
		return totalMoney;
	}

	public void setTotalMoney(double totalMoney) {
		this.totalMoney = totalMoney;
	}

	public double getTotalPay() {
		return totalPay;
	}

	public void setTotalPay(double totalPay) {
		this.totalPay = totalPay;
	}

	public double getDiscount() {
		return discount;
	}

	public void setDiscount(double discount) {
		this.discount = discount;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public String getYJJNumber() {
		return YJJNumber;
	}

	public void setYJJNumber(String yJJNumber) {
		YJJNumber = yJJNumber;
	}

	public String getCreateTimeStr() {
		return createTimeStr;
	}

	public void setCreateTimeStr(String createTimeStr) {
		this.createTimeStr = createTimeStr;
	}
	
	
}
