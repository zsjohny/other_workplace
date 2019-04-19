package com.jiuy.core.meta.clickstatistics;

import java.util.Collection;

public class ClickStatisticsSearch extends ClickStatistics {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6095283308601398489L;

	public ClickStatisticsSearch(){}
	
	
	private long startFloorId;
	
	private long endFloorId;
	
	private Collection<Long> floorIds;
	
	private String floorName;
	
	private String templateId;
	
	private String serialNumber;
	
	private String elementId;
	
	private long startRelatedOrderCount;
	
	private long endRelatedOrderCount;
	
	private long startClickCount;
	
	private long endClickCount;

	

	public long getStartFloorId() {
		return startFloorId;
	}

	public void setStartFloorId(long startFloorId) {
		this.startFloorId = startFloorId;
	}

	public long getEndFloorId() {
		return endFloorId;
	}

	public void setEndFloorId(long endFloorId) {
		this.endFloorId = endFloorId;
	}

	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getElementId() {
		return elementId;
	}

	public void setElementId(String elementId) {
		this.elementId = elementId;
	}

	public long getStartRelatedOrderCount() {
		return startRelatedOrderCount;
	}

	public void setStartRelatedOrderCount(long startRelatedOrderCount) {
		this.startRelatedOrderCount = startRelatedOrderCount;
	}

	public long getEndRelatedOrderCount() {
		return endRelatedOrderCount;
	}

	public void setEndRelatedOrderCount(long endRelatedOrderCount) {
		this.endRelatedOrderCount = endRelatedOrderCount;
	}

	public long getStartClickCount() {
		return startClickCount;
	}

	public void setStartClickCount(long startClickCount) {
		this.startClickCount = startClickCount;
	}

	public long getEndClickCount() {
		return endClickCount;
	}

	public void setEndClickCount(long endClickCount) {
		this.endClickCount = endClickCount;
	}

	public Collection<Long> getFloorIds() {
		return floorIds;
	}

	public void setFloorIds(Collection<Long> floorIds) {
		this.floorIds = floorIds;
	}

	public String getFloorName() {
		return floorName;
	}

	public void setFloorName(String floorName) {
		this.floorName = floorName;
	}
	
	
}
