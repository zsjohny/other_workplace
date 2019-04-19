package com.jiuyuan.entity;

import java.io.Serializable;

import com.jiuyuan.constant.StatisticsPageId;
import com.jiuyuan.constant.StatisticsType;

public class Statistics implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9157044585026860961L;

	private Long id;
	
	private int type;
	
	private Long relatedId;
	
	private String code;
	
	private Long lifeStartTime;
	
	private Long lifeEndTime;
	
	private Integer userClick;
	
	private Integer unknownClick;
	
	private Integer relatedOrderCount;
	
	private Long createTime;
	
	private Long updateTime;
	
	private String element;
	
	private long pageId;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getLifeStartTime() {
		return lifeStartTime;
	}

	public void setLifeStartTime(Long lifeStartTime) {
		this.lifeStartTime = lifeStartTime;
	}

	public Long getLifeEndTime() {
		return lifeEndTime;
	}

	public void setLifeEndTime(Long lifeEndTime) {
		this.lifeEndTime = lifeEndTime;
	}

	public Integer getUserClick() {
		return userClick;
	}

	public void setUserClick(Integer userClick) {
		this.userClick = userClick;
	}

	public Integer getUnknownClick() {
		return unknownClick;
	}

	public void setUnknownClick(Integer unknownClick) {
		this.unknownClick = unknownClick;
	}

	public Integer getRelatedOrderCount() {
		return relatedOrderCount;
	}

	public void setRelatedOrderCount(Integer relatedOrderCount) {
		this.relatedOrderCount = relatedOrderCount;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Long getRelatedId() {
		return relatedId;
	}

	public void setRelatedId(Long relatedId) {
		this.relatedId = relatedId;
	}

	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public long getPageId() {
		return pageId;
	}

	public void setPageId(long pageId) {
		this.pageId = pageId;
	}
	
	
	
	
}
