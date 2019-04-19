package com.jiuyuan.entity.template;

import java.io.Serializable;

import com.jiuyuan.entity.BaseMeta;

public class TemplateAD extends BaseMeta<Long> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 427642928892629641L;

	private long id;
	
	private long templateId;
	
	private long adId;
	
	private int orderIndex;
	
	private int isSelected;
	
	private long createTime;
	
	
	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public long getTemplateId() {
		return templateId;
	}


	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}


	public long getAdId() {
		return adId;
	}


	public void setAdId(long adId) {
		this.adId = adId;
	}


	public int getOrderIndex() {
		return orderIndex;
	}


	public void setOrderIndex(int orderIndex) {
		this.orderIndex = orderIndex;
	}


	public int getIsSelected() {
		return isSelected;
	}


	public void setIsSelected(int isSelected) {
		this.isSelected = isSelected;
	}


	public long getCreateTime() {
		return createTime;
	}


	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}


	@Override
	public Long getCacheId() {
		return null;
	}

}
