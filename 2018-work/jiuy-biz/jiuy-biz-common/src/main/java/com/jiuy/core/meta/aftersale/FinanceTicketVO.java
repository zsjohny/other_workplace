package com.jiuy.core.meta.aftersale;

import java.io.Serializable;

import com.jiuyuan.entity.ServiceTicket;

public class FinanceTicketVO extends FinanceTicket implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -934421293834708784L;
	private long startApplyTime;
	private long endApplyTime;
	private long startCreateTime;
	private long endCreateTime;
	private long startReturnTime;
	private long endReturnTime;
	private double startReturnMoney;
	private double endReturnMoney;

	private ServiceTicket serviceTicket;
	
	public ServiceTicket getServiceTicket() {
		return serviceTicket;
	}

	public void setServiceTicket(ServiceTicket serviceTicket) {
		this.serviceTicket = serviceTicket;
	}

	public FinanceTicketVO(long serviceId, long orderNo, String returnNo, int status, int returnType, double startReturnMoney,
			double endReturnMoney, long startApplyTimeL, long endApplyTimeL, long startCreateTimeL, long endCreateTimeL,
			long startReturnTimeL, long endReturnTimeL) {
		serviceTicket = new ServiceTicket();
		this.serviceTicket.setId(serviceId);
		this.serviceTicket.setOrderNo(orderNo);
		this.setReturnNo(returnNo);
		this.setStatus(status);
		this.setReturnType(returnType);
		this.setStartApplyTime(startApplyTimeL);
		this.setEndApplyTime(endApplyTimeL);
		this.setStartCreateTime(startCreateTimeL);
		this.setEndCreateTime(endCreateTimeL);
		this.setStartReturnTime(startReturnTimeL);
		this.setEndReturnTime(endReturnTimeL);
		this.setStartReturnMoney(startReturnMoney);
		this.setEndReturnMoney(endReturnMoney);
	}

	public long getStartApplyTime() {
		return startApplyTime;
	}

	public void setStartApplyTime(long startApplyTime) {
		this.startApplyTime = startApplyTime;
	}

	public long getEndApplyTime() {
		return endApplyTime;
	}

	public void setEndApplyTime(long endApplyTime) {
		this.endApplyTime = endApplyTime;
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

	public long getStartReturnTime() {
		return startReturnTime;
	}

	public void setStartReturnTime(long startReturnTime) {
		this.startReturnTime = startReturnTime;
	}

	public long getEndReturnTime() {
		return endReturnTime;
	}

	public void setEndReturnTime(long endReturnTime) {
		this.endReturnTime = endReturnTime;
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
}
