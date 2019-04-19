package com.jiuyuan.entity.template;

import java.io.Serializable;

import com.jiuyuan.entity.BaseMeta;

public class TemplateCategory extends BaseMeta<Long> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5144704609945830695L;

	private long id;
	private long templateId;
	private long partnerInnerCatId;
	private int orderIndex;
	private int isSelected;
	private long bannerId;
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



	public long getPartnerInnerCatId() {
		return partnerInnerCatId;
	}



	public void setPartnerInnerCatId(long partnerInnerCatId) {
		this.partnerInnerCatId = partnerInnerCatId;
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



	public long getBannerId() {
		return bannerId;
	}



	public void setBannerId(long bannerId) {
		this.bannerId = bannerId;
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
