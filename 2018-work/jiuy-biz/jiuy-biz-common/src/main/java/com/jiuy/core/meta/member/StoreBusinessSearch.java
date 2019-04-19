package com.jiuy.core.meta.member;

import com.jiuyuan.entity.newentity.StoreBusiness;

public class StoreBusinessSearch extends StoreBusiness{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 7862294320659501627L;
	
	private long createTimeMin;
	
	private long createTimeMax;
	
	private long provinceCode;
	
	private long cityCode;

	private Long membId;
	private Long membEndTime;
	private Integer membDelState;
	private Integer membLevel;
	private Integer membStatus;
	private String membValidTimeQueue;
	private Integer membType;

	public Long getMembId() {
		return membId;
	}

	public void setMembId(Long membId) {
		this.membId = membId;
	}

	public Integer getMembStatus() {
		return membStatus;
	}

	public void setMembStatus(Integer membStatus) {
		this.membStatus = membStatus;
	}

	public Long getMembEndTime() {
		return membEndTime;
	}

	public String getMembValidTimeQueue() {
		return membValidTimeQueue;
	}

	public void setMembValidTimeQueue(String membValidTimeQueue) {
		this.membValidTimeQueue = membValidTimeQueue;
	}

	public Integer getMembType() {
		return membType;
	}

	public void setMembType(Integer membType) {
		this.membType = membType;
	}

	public void setMembEndTime(Long membEndTime) {
		this.membEndTime = membEndTime;
	}

	public Integer getMembDelState() {
		return membDelState;
	}

	public void setMembDelState(Integer membDelState) {
		this.membDelState = membDelState;
	}

	public Integer getMembLevel() {
		return membLevel;
	}

	public void setMembLevel(Integer membLevel) {
		this.membLevel = membLevel;
	}

	public long getCreateTimeMin() {
		return createTimeMin;
	}

	public void setCreateTimeMin(long createTimeMin) {
		this.createTimeMin = createTimeMin;
	}

	public long getCreateTimeMax() {
		return createTimeMax;
	}

	public void setCreateTimeMax(long createTimeMax) {
		this.createTimeMax = createTimeMax;
	}

	public long getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(long provinceCode) {
		this.provinceCode = provinceCode;
	}

	public long getCityCode() {
		return cityCode;
	}

	public void setCityCode(long cityCode) {
		this.cityCode = cityCode;
	}
	
}
