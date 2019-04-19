package com.jiuyuan.entity.product;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiuy.web.controller.util.DateUtil;

public class RestrictionCombination implements Serializable{
	
	public RestrictionCombination() {
		super();
	}

	public RestrictionCombination(long id, String name, String description, int historySetting, int historyBuy, int historyCycle,
			long historyStartTime, int daySetting, int dayBuy, long dayStartTime) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.historySetting = historySetting;
		this.historyBuy = historyBuy;
		this.historyCycle = historyCycle;
		this.historyStartTime = historyStartTime;
		this.daySetting = daySetting;
		this.dayBuy = dayBuy;
		this.dayStartTime = dayStartTime;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2635320488943329949L;

	private long id;

	private String name;
	
	private String description;
	
	private int status;
	
	private int historySetting;
	  
	private int historyBuy;
	  
	private int historyCycle;
  
	private long historyStartTime;
	  
	private int daySetting;
	
	private int dayBuy;
	
	private long dayStartTime;
	
	@JsonIgnore
	private long createTime;
	
	@JsonIgnore
	private long updateTime;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
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
	
	public String getCreateTimeString() {
		if(createTime == 0) {
			return "";
		}
		
		return DateUtil.convertMSEL(createTime);
	}
	
	public int getHistorySetting() {
		return historySetting;
	}

	public void setHistorySetting(int historySetting) {
		this.historySetting = historySetting;
	}

	public int getHistoryBuy() {
		return historyBuy;
	}

	public void setHistoryBuy(int historyBuy) {
		this.historyBuy = historyBuy;
	}

	public int getHistoryCycle() {
		return historyCycle;
	}

	public void setHistoryCycle(int historyCycle) {
		this.historyCycle = historyCycle;
	}

	public long getHistoryStartTime() {
		return historyStartTime;
	}

	public void setHistoryStartTime(long historyStartTime) {
		this.historyStartTime = historyStartTime;
	}

	public int getDaySetting() {
		return daySetting;
	}

	public void setDaySetting(int daySetting) {
		this.daySetting = daySetting;
	}

	public int getDayBuy() {
		return dayBuy;
	}

	public void setDayBuy(int dayBuy) {
		this.dayBuy = dayBuy;
	}

	public long getDayStartTime() {
		return dayStartTime;
	}

	public void setDayStartTime(long dayStartTime) {
		this.dayStartTime = dayStartTime;
	}

	public String getHistoryStartTimeString() {
		return DateUtil.convertMSEL(historyStartTime);
	}
	
	public String getDayStartTimeString() {
		return DateUtil.convertMSEL(dayStartTime);
	}
	
}
