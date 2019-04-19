package com.jiuy.core.meta.clickstatistics;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jiuyuan.entity.BaseMeta;

public class ClickStatistics extends BaseMeta<Object> implements Serializable{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 5836648825983478114L;

	@Override
	public Object getCacheId() {
		return null;
	}
	
	public ClickStatistics(){
		super();
	}

	public ClickStatistics(long id, int type, String code, long lifeStartTime, long lifeEndTime, long userClick,
			long unknownClick, long relatedOrderCount, String element, long relatedId, long createTime,
			long updateTime) {
		super();
		this.id = id;
		this.type = type;
		this.code = code;
		this.lifeStartTime = lifeStartTime;
		this.lifeEndTime = lifeEndTime;
		this.userClick = userClick;
		this.unknownClick = unknownClick;
		this.relatedOrderCount = relatedOrderCount;
		this.element = element;
		this.relatedId = relatedId;
		this.createTime = createTime;
		this.updateTime = updateTime;
	}

	private long id;
	
	private int type;
	
	private String code;
	
	private long lifeStartTime;
	
	private long lifeEndTime;
	
	private long userClick;
	
	private long unknownClick;
	
	private long relatedOrderCount;
	
	private String element;
	
	@JsonIgnore
	private long relatedId;
	
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

	public long getUserClick() {
		return userClick;
	}

	public void setUserClick(long userClick) {
		this.userClick = userClick;
	}

	public long getUnknownClick() {
		return unknownClick;
	}

	public void setUnknownClick(long unknownClick) {
		this.unknownClick = unknownClick;
	}

	public long getRelatedOrderCount() {
		return relatedOrderCount;
	}

	public void setRelatedOrderCount(long relatedOrderCount) {
		this.relatedOrderCount = relatedOrderCount;
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

	public String getElement() {
		return element;
	}

	public void setElement(String element) {
		this.element = element;
	}

	public long getRelatedId() {
		return relatedId;
	}

	public void setRelatedId(long relatedId) {
		this.relatedId = relatedId;
	}
	
}
