package com.jiuyuan.entity.afterSale;

import java.io.Serializable;

import com.jiuyuan.entity.BaseMeta;

public class Settlement extends BaseMeta<Long> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8260356754376521748L;
	
	private long businessId;
	
	private long orderNo;
	
	private long createTime;
	
	private double totalMoney;
	
	private double totalPay;
	
	private double commission;
	
	private long ifReturn;
	
	private String returnReason;
	
	private long returnTime;
	
	private long ifExchange;
	
	private double exchangeMoney;
	
	private String exchangeReason;
	
	private long exchangeTime;
	
	public Settlement() {
		super();
	}

	public Settlement(long businessId, long orderNo, long createTime, double totalMoney, double totalPay,
			double commission, long ifReturn, String returnReason, long returnTime, long ifExchange,
			double exchangeMoney, String exchangeReason, long exchangeTime) {
		this.businessId = businessId;
		this.orderNo = orderNo;
		this.createTime = createTime;
		this.totalMoney = totalMoney;
		this.totalPay = totalPay;
		this.commission = commission;
		this.ifReturn = ifReturn;
		this.returnReason = returnReason;
		this.returnTime = returnTime;
		this.ifExchange = ifExchange;
		this.exchangeMoney = exchangeMoney;
		this.exchangeReason = exchangeReason;
		this.exchangeTime = exchangeTime;
	}


	public long getBusinessId() {
		return businessId;
	}



	public void setBusinessId(long businessId) {
		this.businessId = businessId;
	}



	public long getOrderNo() {
		return orderNo;
	}



	public void setOrderNo(long orderNo) {
		this.orderNo = orderNo;
	}

	
	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
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



	public double getCommission() {
		return commission;
	}



	public void setCommission(double commission) {
		this.commission = commission;
	}



	public long getIfReturn() {
		return ifReturn;
	}



	public void setIfReturn(long ifReturn) {
		this.ifReturn = ifReturn;
	}



	public String getReturnReason() {
		return returnReason;
	}



	public void setReturnReason(String returnReason) {
		this.returnReason = returnReason;
	}


	public long getReturnTime() {
		return returnTime;
	}

	public void setReturnTime(long returnTime) {
		this.returnTime = returnTime;
	}

	public long getIfExchange() {
		return ifExchange;
	}



	public void setIfExchange(long ifExchange) {
		this.ifExchange = ifExchange;
	}



	public double getExchangeMoney() {
		return exchangeMoney;
	}



	public void setExchangeMoney(double exchangeMoney) {
		this.exchangeMoney = exchangeMoney;
	}



	public String getExchangeReason() {
		return exchangeReason;
	}



	public void setExchangeReason(String exchangeReason) {
		this.exchangeReason = exchangeReason;
	}

	public long getExchangeTime() {
		return exchangeTime;
	}

	public void setExchangeTime(long exchangeTime) {
		this.exchangeTime = exchangeTime;
	}

	@Override
	public Long getCacheId() {
		return null;
	}

}
