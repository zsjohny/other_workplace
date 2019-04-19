package com.jiuyuan.entity.afterSale;

import java.io.Serializable;

import com.jiuy.core.meta.order.OrderNew;

public class SettlementOrderNewVO extends OrderNew implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1186090876097281759L;
	
	public long startTime;
	
	public long endTime;
	
	public double startTotalPay;
	
	public double endTotalPay;
	
	public double startCommission;
	
	public double endCommission;
	
	private double startAvailableCommission;
	
	private double endAvailableCommission;

	public SettlementOrderNewVO(long businessId, long orderNo, long yjjNumber, double startTotalPay,
			double endTotalPay, double startCommission, double endCommission, double startAvailableCommission,
			double endAvailableCommission, long startTime, long endTime,int orderType) {
		this.setBelongBusinessId(businessId);
		this.setOrderNo(orderNo);
		this.setYjjNumber(yjjNumber);
		this.setOrderType(orderType);
		this.startTotalPay=startTotalPay;
		this.endTotalPay=endTotalPay;
		this.startCommission=startCommission;
		this.endCommission=endCommission;
		this.startAvailableCommission=startAvailableCommission;
		this.endAvailableCommission=endAvailableCommission;
		this.startTime=startTime;
		this.endTime=endTime;
	}

	
	public double getStartAvailableCommission() {
		return startAvailableCommission;
	}


	public void setStartAvailableCommission(double startAvailableCommission) {
		this.startAvailableCommission = startAvailableCommission;
	}


	public double getEndAvailableCommission() {
		return endAvailableCommission;
	}


	public void setEndAvailableCommission(double endAvailableCommission) {
		this.endAvailableCommission = endAvailableCommission;
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

	public double getStartTotalPay() {
		return startTotalPay;
	}

	public void setStartTotalPay(double startTotalPay) {
		this.startTotalPay = startTotalPay;
	}

	public double getEndTotalPay() {
		return endTotalPay;
	}

	public void setEndTotalPay(double endTotalPay) {
		this.endTotalPay = endTotalPay;
	}

	public double getStartCommission() {
		return startCommission;
	}

	public void setStartCommission(double startCommission) {
		this.startCommission = startCommission;
	}

	public double getEndCommission() {
		return endCommission;
	}

	public void setEndCommission(double endCommission) {
		this.endCommission = endCommission;
	}
	
}
