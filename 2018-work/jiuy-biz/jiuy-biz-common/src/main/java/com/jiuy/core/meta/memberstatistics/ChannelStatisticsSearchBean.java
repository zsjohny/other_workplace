package com.jiuy.core.meta.memberstatistics;

import java.io.Serializable;

/**
* @author WuWanjian
* @version 创建时间: 2017年2月23日 上午10:56:09
*/
public class ChannelStatisticsSearchBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -3881093224501188988L;

	private String yjjNumber;
	
	private String superiorYjjNumber;
	
	private int userStatus;
	
	private int orderCountMin;
	
	private int orderCountMax;
	
	private int pvCountMin;
	
	private int pvCountMax;
	
	private long startCreateTime;
	
	private long endCreateTime;
	
	private int userType;
	
	private int clientSrc;
	
	public ChannelStatisticsSearchBean() {
		super();
	}

	public ChannelStatisticsSearchBean(String yjjNumber, String superiorYjjNumber, int userStatus, int orderCountMin,
			int orderCountMax, int pvCountMin, int pvCountMax, long startCreateTime, long endCreateTime, int userType,
			int clientSrc) {
		super();
		this.yjjNumber = yjjNumber;
		this.superiorYjjNumber = superiorYjjNumber;
		this.userStatus = userStatus;
		this.orderCountMin = orderCountMin;
		this.orderCountMax = orderCountMax;
		this.pvCountMin = pvCountMin;
		this.pvCountMax = pvCountMax;
		this.startCreateTime = startCreateTime;
		this.endCreateTime = endCreateTime;
		this.userType = userType;
		this.clientSrc = clientSrc;
	}

	public String getYjjNumber() {
		return yjjNumber;
	}

	public void setYjjNumber(String yjjNumber) {
		this.yjjNumber = yjjNumber;
	}

	public String getSuperiorYjjNumber() {
		return superiorYjjNumber;
	}

	public void setSuperiorYjjNumber(String superiorYjjNumber) {
		this.superiorYjjNumber = superiorYjjNumber;
	}

	public int getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}

	public int getOrderCountMin() {
		return orderCountMin;
	}

	public void setOrderCountMin(int orderCountMin) {
		this.orderCountMin = orderCountMin;
	}

	public int getOrderCountMax() {
		return orderCountMax;
	}

	public void setOrderCountMax(int orderCountMax) {
		this.orderCountMax = orderCountMax;
	}

	public int getPvCountMin() {
		return pvCountMin;
	}

	public void setPvCountMin(int pvCountMin) {
		this.pvCountMin = pvCountMin;
	}

	public int getPvCountMax() {
		return pvCountMax;
	}

	public void setPvCountMax(int pvCountMax) {
		this.pvCountMax = pvCountMax;
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

	public int getUserType() {
		return userType;
	}

	public void setUserType(int userType) {
		this.userType = userType;
	}

	public int getClientSrc() {
		return clientSrc;
	}

	public void setClientSrc(int clientSrc) {
		this.clientSrc = clientSrc;
	}
	
	
}
