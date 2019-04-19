package com.jiuy.core.meta.aftersale;

import java.io.Serializable;

import com.jiuyuan.entity.BaseMeta;

public class StoreSettlement extends BaseMeta<Long> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1412470791041710833L;
	
	private long businessNumber;
	
	private String businessName;
	
	private long memberNumber;
	
	private long orderNum;
	
	private double totalPay;
	
	private double totalCommission;
	
	private long id;
	
	private double availableBalance;
	
	private long createTime;
	

	public double getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(double availableBalance) {
		this.availableBalance = availableBalance;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public StoreSettlement() {
		super();
	}


	public double getTotalCommission() {
		return totalCommission;
	}



	public void setTotalCommission(double totalCommission) {
		this.totalCommission = totalCommission;
	}



	public long getId() {
		return id;
	}



	public void setId(long id) {
		this.id = id;
	}



	public long getBusinessNumber() {
		return businessNumber;
	}



	public void setBusinessNumber(long businessNumber) {
		this.businessNumber = businessNumber;
	}



	public String getBusinessName() {
		return businessName;
	}



	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}



	public long getMemberNumber() {
		return memberNumber;
	}



	public void setMemberNumber(long memberNumber) {
		this.memberNumber = memberNumber;
	}



	public long getOrderNum() {
		return orderNum;
	}



	public void setOrderNum(long orderNum) {
		this.orderNum = orderNum;
	}



	public double getTotalPay() {
		return totalPay;
	}



	public void setTotalPay(double totalPay) {
		this.totalPay = totalPay;
	}

	@Override
	public Long getCacheId() {
		return null;
	}

}
