package com.jiuy.core.meta.member;

import java.io.Serializable;

public class MemberSearch extends Member implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2410179185461406566L;

	private long bindPhone;
	
	private long yjjNumber;
	
	private int status;
	
	private long createTimeMin;
	
	private long createTimeMax;
	
	private int PartnerCountMin;
	
	private int partnerCountMax;
	
	private int jiuCoinMin;
	
	private int jiuCoinMax;

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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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

	public int getPartnerCountMin() {
		return PartnerCountMin;
	}

	public void setPartnerCountMin(int partnerCountMin) {
		PartnerCountMin = partnerCountMin;
	}

	public int getPartnerCountMax() {
		return partnerCountMax;
	}

	public void setPartnerCountMax(int partnerCountMax) {
		this.partnerCountMax = partnerCountMax;
	}

	public int getJiuCoinMin() {
		return jiuCoinMin;
	}

	public void setJiuCoinMin(int jiuCoinMin) {
		this.jiuCoinMin = jiuCoinMin;
	}

	public int getJiuCoinMax() {
		return jiuCoinMax;
	}

	public void setJiuCoinMax(int jiuCoinMax) {
		this.jiuCoinMax = jiuCoinMax;
	}
	
	
}
