package com.jiuy.core.meta.member;

import java.io.Serializable;

public class MemberVO extends Member implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -354137829201580755L;
	
	private long bindPhone;
	
	private long yjjNumber;
	
	private int jiuCoinBalance;
	
	private int status;

	private long businessNumber;
	
	private int userType;
	
	private String userRelatedName;
	
	private long parentYJJNumber;
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getJiuCoinBalance() {
		return jiuCoinBalance;
	}

	public void setJiuCoinBalance(int jiuCoinBalance) {
		this.jiuCoinBalance = jiuCoinBalance;
	}

	public long getBindPhone() {
		return bindPhone;
	}

	public void setBindPhone(long bindPhone) {
		this.bindPhone = bindPhone;
	}

	public long getYjjNumber() {
		return yjjNumber;
	}

	public void setYjjNumber(long yjjNumber) {
		this.yjjNumber = yjjNumber;
	}

	public long getBusinessNumber() {
		return businessNumber;
	}

	public void setBusinessNumber(long businessNumber) {
		this.businessNumber = businessNumber;
	}

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public String getUserRelatedName() {
		return userRelatedName;
	}

	public void setUserRelatedName(String userRelatedName) {
		this.userRelatedName = userRelatedName;
	}

	public long getParentYJJNumber() {
		return parentYJJNumber;
	}

	public void setParentYJJNumber(long parentYJJNumber) {
		this.parentYJJNumber = parentYJJNumber;
	}
	
	
}
