package com.jiuyuan.entity.withdraw;

import java.io.Serializable;

public class WithDrawApplyVO extends WithDrawApply implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 6415667604320812752L;

    public double startReturnMoney;
	
	public double endReturnMoney;
	
	public double startApplyMoney;
		
	public double endApplyMoney;
	
	public long startDealTime;
	
	public long endDealTime;
	
	public long startCreateTime;
	
	public long endCreateTime;
	

	public WithDrawApplyVO(long tradeId, String tradeNo, int status,int type, double startReturnMoney,
			double endReturnMoney, double startApplyMoney,
			double endApplyMoney,long startDealTime, long endDealTime,
			long startCreateTime, long endCreateTime) {
		this.setTradeId(tradeId);
		this.setTradeNo(tradeNo);
		this.setStatus(status);
		this.setType(type);
		this.startReturnMoney = startReturnMoney;
		this.endReturnMoney = endReturnMoney;
		this.startApplyMoney=startApplyMoney;
		this.endApplyMoney=endApplyMoney;
		this.startDealTime = startDealTime;
		this.endDealTime = endDealTime;
		this.startCreateTime = startCreateTime;
		this.endCreateTime = endCreateTime;
	}


	public double getStartReturnMoney() {
		return startReturnMoney;
	}


	public void setStartReturnMoney(double startReturnMoney) {
		this.startReturnMoney = startReturnMoney;
	}


	public double getEndReturnMoney() {
		return endReturnMoney;
	}


	public void setEndReturnMoney(double endReturnMoney) {
		this.endReturnMoney = endReturnMoney;
	}


	public long getStartDealTime() {
		return startDealTime;
	}


	public void setStartDealTime(long startDealTime) {
		this.startDealTime = startDealTime;
	}


	public long getEndDealTime() {
		return endDealTime;
	}


	public void setEndDealTime(long endDealTime) {
		this.endDealTime = endDealTime;
	}


	public double getStartApplyMoney() {
		return startApplyMoney;
	}


	public void setStartApplyMoney(double startApplyMoney) {
		this.startApplyMoney = startApplyMoney;
	}


	public double getEndApplyMoney() {
		return endApplyMoney;
	}


	public void setEndApplyMoney(double endApplyMoney) {
		this.endApplyMoney = endApplyMoney;
	}


	public long getStartCreateTime() {
		return startCreateTime;
	}


	public void setStartCreateTime(long startCreateTime) {
		this.startCreateTime = startCreateTime;
	}


	public long getEndCreateTime() {
		return endCreateTime;
	}


	public void setEndCreateTime(long endCreateTime) {
		this.endCreateTime = endCreateTime;
	}


	@Override
	public String toString() {
		return "WithDrawApplyVO [startReturnMoney=" + startReturnMoney + ", endReturnMoney=" + endReturnMoney
				+ ", startApplyMoney=" + startApplyMoney + ", endApplyMoney=" + endApplyMoney + ", startDealTime="
				+ startDealTime + ", endDealTime=" + endDealTime + ", startCreateTime=" + startCreateTime
				+ ", endCreateTime=" + endCreateTime + "]";
	}
	
	
	
}
