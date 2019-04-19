package com.jiuyuan.entity;
/**
* @author WuWanjian
* @version 创建时间: 2017年4月20日 上午10:00:45
*/
public class JiuCoinDeductStatisticsDayBean {
	
	private String name;
	
	private int orderCount;
	
	private double orderMoney;
	
	private int payOrderCount;
	
	private double payOrderMoney;
	
	private double deductMoney;
	
	private int deductCoinNum;
	
	public JiuCoinDeductStatisticsDayBean(){}
	
	public JiuCoinDeductStatisticsDayBean(JiuCoinDeductStatisticsDayBean bean1, JiuCoinDeductStatisticsDayBean bean2){
		this.orderCount = bean1.orderCount + bean2.orderCount;
		this.orderMoney = bean1.orderMoney + bean2.orderMoney;
		this.payOrderCount = bean1.payOrderCount + bean2.payOrderCount;
		this.payOrderMoney = bean1.payOrderMoney + bean2.payOrderMoney;
		this.deductMoney = bean1.deductMoney + bean2.deductMoney;
		this.deductCoinNum = bean1.deductCoinNum + bean2.deductCoinNum;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	public double getOrderMoney() {
		return orderMoney;
	}

	public void setOrderMoney(double orderMoney) {
		this.orderMoney = orderMoney;
	}

	public int getPayOrderCount() {
		return payOrderCount;
	}

	public void setPayOrderCount(int payOrderCount) {
		this.payOrderCount = payOrderCount;
	}

	public double getPayOrderMoney() {
		return payOrderMoney;
	}

	public void setPayOrderMoney(double payOrderMoney) {
		this.payOrderMoney = payOrderMoney;
	}

	public double getDeductMoney() {
		return deductMoney;
	}

	public void setDeductMoney(double deductMoney) {
		this.deductMoney = deductMoney;
	}

	public int getDeductCoinNum() {
		return deductCoinNum;
	}

	public void setDeductCoinNum(int deductCoinNum) {
		this.deductCoinNum = deductCoinNum;
	}
	
	
}
