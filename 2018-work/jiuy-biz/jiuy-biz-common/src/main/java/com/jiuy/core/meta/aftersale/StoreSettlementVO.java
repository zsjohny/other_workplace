package com.jiuy.core.meta.aftersale;

import java.io.Serializable;
import java.util.List;

public class StoreSettlementVO extends StoreSettlement implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5205243498907709081L;
	
	private long startSettlementTime;
	
	private long endSettlementTime;
	
	private List<String> businessIds;
	
	private long startMember;
	
	private long endMember;
	
	private long startOrder;
	
	private long endOrder;
	
	private double startPay;
	
	private double endPay;
	
	private double startCommission;
	
	private double endCommission;
	
	private double startBalance;
	
	private double endBalance;

	public StoreSettlementVO(long startSettlementTimeL, long endSettlementTimeL, List<String> listId, long startMember,
			long endMember, long startOrder, long endOrder, double startPay, double endPay, double startCommission,
			double endCommission, double startBalance, double endBalance) {
		this.startSettlementTime = startSettlementTimeL;
		this.endSettlementTime = endSettlementTimeL;
		this.businessIds = listId;
		this.startMember=startMember;
		this.endMember=endMember;
		this.startOrder=startOrder;
		this.endOrder=endOrder;
		this.startPay=startPay;
		this.endPay=endPay;
		this.startCommission=startCommission;
		this.endCommission=endCommission;
		this.startBalance=startBalance;
		this.endBalance=endBalance;
	}

	public long getStartMember() {
		return startMember;
	}



	public void setStartMember(long startMember) {
		this.startMember = startMember;
	}



	public long getEndMember() {
		return endMember;
	}



	public void setEndMember(long endMember) {
		this.endMember = endMember;
	}



	public long getStartOrder() {
		return startOrder;
	}



	public void setStartOrder(long startOrder) {
		this.startOrder = startOrder;
	}



	public long getEndOrder() {
		return endOrder;
	}



	public void setEndOrder(long endOrder) {
		this.endOrder = endOrder;
	}



	public double getStartPay() {
		return startPay;
	}



	public void setStartPay(double startPay) {
		this.startPay = startPay;
	}



	public double getEndPay() {
		return endPay;
	}



	public void setEndPay(double endPay) {
		this.endPay = endPay;
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



	public double getStartBalance() {
		return startBalance;
	}



	public void setStartBalance(double startBalance) {
		this.startBalance = startBalance;
	}



	public double getEndBalance() {
		return endBalance;
	}



	public void setEndBalance(double endBalance) {
		this.endBalance = endBalance;
	}



	public long getStartSettlementTime() {
		return startSettlementTime;
	}

	public void setStartSettlementTime(long startSettlementTime) {
		this.startSettlementTime = startSettlementTime;
	}

	public long getEndSettlementTime() {
		return endSettlementTime;
	}

	public void setEndSettlementTime(long endSettlementTime) {
		this.endSettlementTime = endSettlementTime;
	}

	public List<String> getBusinessIds() {
		return businessIds;
	}

	public void setBusinessIds(List<String> businessIds) {
		this.businessIds = businessIds;
	}

}
