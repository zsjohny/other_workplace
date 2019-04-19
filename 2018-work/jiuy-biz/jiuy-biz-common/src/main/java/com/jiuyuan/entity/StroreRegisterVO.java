package com.jiuyuan.entity;

import java.io.Serializable;

public class StroreRegisterVO extends StoreRegister implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5929494653656221183L;
	private long startApplyTime;
	private long endApplyTime;
	private long startCreateTime;
	private long endCreateTime;
	public StroreRegisterVO(long applyId, String phoneNumber, String businessName, int status, int businessType,String companyName,
			long startCreateTimeL, long endCreateTimeL, String idCardNumber, String businessAddress, String legalPerson,
			long startApplyTimeL, long endApplyTimeL,long superStoreId) {
		this.setId(applyId);
		this.setPhoneNumber(phoneNumber);
		this.setBusinessName(businessName);
		this.setStatus(status);
		this.setBusinessType(businessType);
		this.setCompanyName(companyName);
		this.setIdCardNumber(idCardNumber);
		this.setBusinessAddress(businessAddress);
		this.setLegalPerson(legalPerson);
		this.startCreateTime=startCreateTimeL;
		this.endCreateTime=endCreateTimeL;
		this.startApplyTime=startApplyTimeL;
		this.endApplyTime=endApplyTimeL;
		this.setSuperBusinessId(superStoreId);
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
	
}
