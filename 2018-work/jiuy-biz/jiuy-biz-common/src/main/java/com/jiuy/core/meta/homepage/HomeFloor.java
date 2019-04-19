package com.jiuy.core.meta.homepage;

import java.io.Serializable;

import com.jiuyuan.entity.BaseMeta;

public class HomeFloor extends BaseMeta<Long>  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 9000021663927957978L;

	private Long id;
	
	private Long homeTemplateId;
	
	private Integer status;
	
	private Integer publishStatus;
	
	private String description;
	
	private Integer weight;
	
	private String name;
	
	private Integer showName;
	
	private Integer hasSpacing;
	
	private Long nextHomeTemplateId;
	
	private Integer nextShowName;
	
	private Integer nextHasSpacing;

	private Long createTime;
	
	private Long updateTime;
	
	private Integer type;
	
	private Long relatedId;
	
	private Integer isShow;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getHomeTemplateId() {
		return homeTemplateId;
	}

	public void setHomeTemplateId(Long homeTemplateId) {
		this.homeTemplateId = homeTemplateId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(Integer publishStatus) {
		this.publishStatus = publishStatus;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getShowName() {
		return showName;
	}

	public void setShowName(Integer showName) {
		this.showName = showName;
	}

	public Integer getHasSpacing() {
		return hasSpacing;
	}

	public void setHasSpacing(Integer hasSpacing) {
		this.hasSpacing = hasSpacing;
	}

	public Long getNextHomeTemplateId() {
		return nextHomeTemplateId;
	}

	public void setNextHomeTemplateId(Long nextHomeTemplateId) {
		this.nextHomeTemplateId = nextHomeTemplateId;
	}

	public Integer getNextShowName() {
		return nextShowName;
	}

	public void setNextShowName(Integer nextShowName) {
		this.nextShowName = nextShowName;
	}

	public Integer getNextHasSpacing() {
		return nextHasSpacing;
	}

	public void setNextHasSpacing(Integer nextHasSpacing) {
		this.nextHasSpacing = nextHasSpacing;
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

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Long getRelatedId() {
		return relatedId;
	}

	public void setRelatedId(Long relatedId) {
		this.relatedId = relatedId;
	}

	public Integer getIsShow() {
		return isShow;
	}

	public void setIsShow(Integer isShow) {
		this.isShow = isShow;
	}

	@Override
	public Long getCacheId() {
		return null;
	}
	
	
}
