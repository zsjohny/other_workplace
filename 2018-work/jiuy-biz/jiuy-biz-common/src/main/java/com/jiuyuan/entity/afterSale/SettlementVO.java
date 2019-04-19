package com.jiuyuan.entity.afterSale;

import java.io.Serializable;

public class SettlementVO extends Settlement implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1186090876097281759L;
	
	public long yjjNumber;
	
	public long startTime;
	
	public long endTime;
	
	public double startTotalPay;
	
	public double endTotalPay;
	
	public double startCommission;
	
	public double endCommission;

	public SettlementVO(long businessId, long orderNo, long yjjNumber, double startTotalPay, double endTotalPay,
			double startCommission, double endCommission, long startTimeL, long endTimeL) {
		this.setBusinessId(businessId);
		this.setOrderNo(orderNo);
		this.setYjjNumber(yjjNumber);
		this.startTotalPay=startTotalPay;
		this.endTotalPay=endTotalPay;
		this.startCommission=startCommission;
		this.endCommission=endCommission;
		this.startTime=startTimeL;
		this.endTime=endTimeL;
	}

	public long getYjjNumber() {
		return yjjNumber;
	}

	public void setYjjNumber(long yjjNumber) {
		this.yjjNumber = yjjNumber;
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
