package com.jiuyuan.entity.brand;

import java.io.Serializable;

import com.jiuyuan.entity.BaseMeta;

public class PartnerCatManage extends BaseMeta<Long> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4999351305364384816L;

	private long id;
	
	private String name;
	
	private String description;
	
	private int weight;
	
	private int status;
	
	private int orderIndex;
	
	private int isSelected;
	
	
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


	public int getWeight() {
		return weight;
	}


	public void setWeight(int weight) {
		this.weight = weight;
	}


	public int getStatus() {
		return status;
	}


	public void setStatus(int status) {
		this.status = status;
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

	@Override
	public Long getCacheId() {
		return null;
	}

}
