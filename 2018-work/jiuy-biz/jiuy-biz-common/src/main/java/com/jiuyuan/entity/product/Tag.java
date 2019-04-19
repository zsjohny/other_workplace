package com.jiuyuan.entity.product;

import java.io.Serializable;
import java.util.List;

public class Tag implements Serializable{
	
	public Tag() {
	}

	public Tag(String name, Integer priority, Long groupId, String description) {
		this.name = name;
		this.priority = priority;
		this.groupId = groupId;
		this.description = description;
	}
	
	public Tag(Long id, String name, Integer priority, Long groupId, String description) {
		this(name, priority, groupId, description);
		this.id = id;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -6817818673588275837L;

	private long id;
	
	private String name;
	
	private int priority;
	
	private long groupId;
	
	private String description;
	
	private int count;
	
	private int status;
	
	private long createTime;
	
	private long updateTime;
	
	private long topTime;
	
	private List<Tag> childTagList;
	
	public long getTopTime() {
		return topTime;
	}

	public void setTopTime(long topTime) {
		this.topTime = topTime;
	}

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

	public int getPriority() {
		return priority;
	}

	public void setPriority(int priority) {
		this.priority = priority;
	}

	public long getGroupId() {
		return groupId;
	}

	public void setGroupId(long groupId) {
		this.groupId = groupId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
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

	public List<Tag> getChildTagList() {
		return childTagList;
	}

	public void setChildTagList(List<Tag> childTagList) {
		this.childTagList = childTagList;
	}
	
	
	
}
