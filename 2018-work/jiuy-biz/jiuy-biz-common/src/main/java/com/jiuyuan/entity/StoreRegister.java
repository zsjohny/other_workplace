package com.jiuyuan.entity;

import java.io.Serializable;

public class StoreRegister implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7539041937458699530L;

	private long id;

	private long adminId;

	private String adminName;

	private long createId;

	private String createName;

	private String businessName;

	private long businessId;

	private int businessType;

	private String legalPerson;

	private String idCardNumber;

	private String businessAddress;

	private String phoneNumber;

	private String companyName;

	private int status;

	private String applyMemo;

	private long applyTime;

	private long createTime;

	private long updateTime;

	private long businessNumber;

	private long accountTime;

	// added by chenyuan 2017-5-6
	private long superBusinessId;// 上级商家号

	private String superStoreNameAndNumber;// 上级商家名称及号码

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getAdminId() {
		return adminId;
	}

	public void setAdminId(long adminId) {
		this.adminId = adminId;
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public long getCreateId() {
		return createId;
	}

	public void setCreateId(long createId) {
		this.createId = createId;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(long businessId) {
		this.businessId = businessId;
	}

	public int getBusinessType() {
		return businessType;
	}

	public void setBusinessType(int businessType) {
		this.businessType = businessType;
	}

	public String getLegalPerson() {
		return legalPerson;
	}

	public void setLegalPerson(String legalPerson) {
		this.legalPerson = legalPerson;
	}

	public String getIdCardNumber() {
		return idCardNumber;
	}

	public void setIdCardNumber(String idCardNumber) {
		this.idCardNumber = idCardNumber;
	}

	public String getBusinessAddress() {
		return businessAddress;
	}

	public void setBusinessAddress(String businessAddress) {
		this.businessAddress = businessAddress;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getApplyMemo() {
		return applyMemo;
	}

	public void setApplyMemo(String applyMemo) {
		this.applyMemo = applyMemo;
	}

	public long getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(long applyTime) {
		this.applyTime = applyTime;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(long updateTime) {
		this.updateTime = updateTime;
	}

	public long getBusinessNumber() {
		return businessNumber;
	}

	public void setBusinessNumber(long businessNumber) {
		this.businessNumber = businessNumber;
	}

	public long getAccountTime() {
		return accountTime;
	}

	public void setAccountTime(long accountTime) {
		this.accountTime = accountTime;
	}

	public long getSuperBusinessId() {
		return superBusinessId;
	}

	public void setSuperBusinessId(long superBusinessId) {
		this.superBusinessId = superBusinessId;
	}

	public String getSuperStoreNameAndNumber() {
		return superStoreNameAndNumber;
	}

	public void setSuperStoreNameAndNumber(String superStoreNameAndNumber) {
		this.superStoreNameAndNumber = superStoreNameAndNumber;
	}

}
