package com.jiuy.core.meta.memberstatistics;

import com.jiuy.web.controller.util.DateUtil;

/**
* @author WuWanjian
* @version 创建时间: 2017年2月23日 上午11:12:14
*/
public class ChannelStatisticsBean {
	private String YJJNumber;
	
	private String superiorYjjNumber;
	
	private String storeNumber;
	
	private long userId;
	
	private int userStatus;
	
	private int pvCount;
	
	private int orderCount;
	
	private long userCreateTime;
	
	private long lastTimeVist;

	public String getYJJNumber() {
		return YJJNumber;
	}

	public void setYJJNumber(String YJJNumber) {
		this.YJJNumber = YJJNumber;
	}
	
	public String getSuperiorYjjNumber() {
		return superiorYjjNumber;
	}

	public void setSuperiorYjjNumber(String superiorYjjNumber) {
		this.superiorYjjNumber = superiorYjjNumber;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	public int getUserStatus() {
		return userStatus;
	}

	public void setUserStatus(int userStatus) {
		this.userStatus = userStatus;
	}
	
	public String getUserMemberTypeStr(){
		return this.storeNumber.equals("0") ? "普通会员" : "门店会员";
	}	
	
	public int getUserMemberType(){
		return this.storeNumber.equals("0") ? 0 : 1;
	}

	public int getPvCount() {
		return pvCount;
	}

	public void setPvCount(int pvCount) {
		this.pvCount = pvCount;
	}

	public int getOrderCount() {
		return orderCount;
	}

	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}

	public long getUserCreateTime() {
		return userCreateTime;
	}

	public void setUserCreateTime(long userCreateTime) {
		this.userCreateTime = userCreateTime;
	}

	public long getLastTimeVist() {
		return lastTimeVist;
	}

	public void setLastTimeVist(long lastTimeVist) {
		this.lastTimeVist = lastTimeVist;
	}

	public String getStoreNumber() {
		return storeNumber;
	}

	public void setStoreNumber(String storeNumber) {
		this.storeNumber = storeNumber;
	}
	
	public String getCreateTimeStr(){
		if(userCreateTime==0){
			return "无";
		}else{
			return DateUtil.convertMSEL(userCreateTime);
		}
	}
	
	public String getLastTimeVistStr(){
		if(lastTimeVist==0){
			return "无";
		}else{
			return DateUtil.convertMSEL(lastTimeVist);
		}
	}
}
