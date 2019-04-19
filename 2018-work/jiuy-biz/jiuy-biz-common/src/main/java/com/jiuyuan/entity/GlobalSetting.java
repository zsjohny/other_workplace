package com.jiuyuan.entity;

import java.io.Serializable;

public class GlobalSetting extends BaseMeta<Long> implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	private String propertyName;
	private String propertyValue;
	private int Status;
	private long createTime;
	private long updateTime;
	private int groupId;
	private String groupName;
	private String description;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public int getStatus() {
		return Status;
	}

	public void setStatus(int status) {
		Status = status;
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

	@Override
	public Long getCacheId() {
		return null;
	}
	
	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "GlobalSetting [id=" + id + ", propertyName=" + propertyName + ", propertyValue=" + propertyValue
				+ ", Status=" + Status + ", createTime=" + createTime + ", updateTime=" + updateTime + ", groupId="
				+ groupId + ", groupName=" + groupName + ", description=" + description + "]";
	}

}
