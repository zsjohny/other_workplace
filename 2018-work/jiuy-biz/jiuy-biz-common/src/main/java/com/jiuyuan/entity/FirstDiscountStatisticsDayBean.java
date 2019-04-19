package com.jiuyuan.entity;
/**
* @author WuWanjian
* @version 创建时间: 2017年4月6日 上午10:07:16
*/
public class FirstDiscountStatisticsDayBean {
	
	private String name;
	
	private int goodsCount;
	
	private int orderCount;
	
	private int payOrderCount;
	
	private double payMoney;
	
	private double discountMoney;
	
	public FirstDiscountStatisticsDayBean(){}
	
	public FirstDiscountStatisticsDayBean(FirstDiscountStatisticsDayBean bean1, FirstDiscountStatisticsDayBean bean2){
		this.goodsCount = bean1.getGoodsCount() + bean2.getGoodsCount();
		this.orderCount = bean1.getOrderCount() + bean2.getOrderCount();
		this.payOrderCount = bean1.getPayOrderCount() + bean2.getPayOrderCount();
		this.payMoney = bean1.getPayMoney() + bean2.getPayMoney();
		this.discountMoney = bean1.getDiscountMoney() + bean2.getDiscountMoney();
	}
	
	public int getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(int goodsCount) {
		this.goodsCount = goodsCount;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	public int getPayOrderCount() {
		return payOrderCount;
	}

	public void setPayOrderCount(int payOrderCount) {
		this.payOrderCount = payOrderCount;
	}

	public double getPayMoney() {
		return payMoney;
	}

	public void setPayMoney(double payMoney) {
		this.payMoney = payMoney;
	}

	public double getDiscountMoney() {
		return discountMoney;
	}

	public void setDiscountMoney(double discountMoney) {
		this.discountMoney = discountMoney;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
}
