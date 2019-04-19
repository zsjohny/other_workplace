package com.yujj.entity.homepage;

import java.io.Serializable;

public class HomeFloorVO implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 3299308017164685688L;
	
	private long id;
	
	private String name;
	
	private long homeTemplateId;
	
	private int status;
	
	private int type;
	
	private long createTime;
	
	private long updateTime;
	
	private String description;
	
	private int weight;
	
	private int showName;
	
	private int hasSpacing;
	
	private String templateName;
	
	private String content;
	
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	public int getStatus() {
		return status;
	}
	
	public void setStatus(int status) {
		this.status = status;
	}
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
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
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public long getHomeTemplateId() {
		return homeTemplateId;
	}
	
	public void setHomeTemplateId(long homeTemplateId) {
		this.homeTemplateId = homeTemplateId;
	}
	
	public int getShowName() {
		return showName;
	}
	
	public void setShowName(int showName) {
		this.showName = showName;
	}
	
	public int getHasSpacing() {
		return hasSpacing;
	}
	
	public void setHasSpacing(int hasSpacing) {
		this.hasSpacing = hasSpacing;
	}
	
	public String getTemplateName() {
		return templateName;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
}
