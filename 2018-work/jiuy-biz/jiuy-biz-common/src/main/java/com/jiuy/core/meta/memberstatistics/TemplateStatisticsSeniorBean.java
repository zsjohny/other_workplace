package com.jiuy.core.meta.memberstatistics;

import com.jiuy.web.controller.util.DateUtil;

/**
* @author WuWanjian
* @version 创建时间: 2017年3月9日 上午9:27:24
*/
public class TemplateStatisticsSeniorBean {
	private String code;
	
	private String floorName = "";
	
	private String templateId;
	
	private String serialNumber="";
	
	private int pv;
	
	private int uv;
	
	private int ipCount;
	
	private int userCount;
	
	private int relatedOrderCount;
	
	private double relatedTotalOrderMoney;
	
	private String relatedOrderNos="无";
	
	private double conversion;
	
	private long lifeStartTime;
	
	private long lifeEndTime;
	
	private String liftStartTimeStr;
	
	private String liftEndTimeStr;
	
	public String getLiftStartTimeStr() {
		return DateUtil.convertMSEL(lifeStartTime);
	}

	public String getLiftEndTimeStr() {
		return DateUtil.convertMSEL(lifeEndTime);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}

	public String getTemplateId() {
		String[] parsingCode = parsingCode(code);
		return parsingCode[2];
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getSerialNumber() {
		String[] parsingCode = parsingCode(code);
		return parsingCode[3];
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public int getPv() {
		return pv;
	}

	public void setPv(int pv) {
		this.pv = pv;
	}

	public int getUv() {
		return uv;
	}

	public void setUv(int uv) {
		this.uv = uv;
	}

	public int getIpCount() {
		return ipCount;
	}

	public void setIpCount(int ipCount) {
		this.ipCount = ipCount;
	}

	public int getUserCount() {
		return userCount;
	}

	public void setUserCount(int userCount) {
		this.userCount = userCount;
	}

	public int getRelatedOrderCount() {
		return relatedOrderCount;
	}

	public void setRelatedOrderCount(int relatedOrderCount) {
		this.relatedOrderCount = relatedOrderCount;
	}

	public double getRelatedTotalOrderMoney() {
		return relatedTotalOrderMoney;
	}

	public void setRelatedTotalOrderMoney(double relatedTotalOrderMoney) {
		this.relatedTotalOrderMoney = relatedTotalOrderMoney;
	}

	public double getConversion() {
		return conversion;
	}

	public void setConversion(double conversion) {
		this.conversion = conversion;
	}

	public long getLifeStartTime() {
		return lifeStartTime;
	}

	public void setLifeStartTime(long lifeStartTime) {
		this.lifeStartTime = lifeStartTime;
	}

	public long getLifeEndTime() {
		return lifeEndTime;
	}

	public void setLifeEndTime(long lifeEndTime) {
		this.lifeEndTime = lifeEndTime;
	}

	private String[] parsingCode(String code){
		String[] codeArray = code.split("L|M|N|ID");
		return codeArray;
	}

	public String getRelatedOrderNos() {
		return relatedOrderNos;
	}

	public void setRelatedOrderNos(String relatedOrderNos) {
		this.relatedOrderNos = relatedOrderNos;
	}
	
	
}
