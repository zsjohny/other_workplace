package com.jiuy.core.meta;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SearchKeyword {
	
	private Long id;
	
	private String keyword;
	
	private Integer searchCount;
	
	private Integer searchResultCount;
	
	private Integer weightType;
	
	private Integer weight;
	
	private Integer type;
	
	private Integer status;
	
	@JsonIgnore
	private Long createTime;
	
	@JsonIgnore
	private Long updateTime;
	
	public SearchKeyword() {
	}

	public SearchKeyword(String keyword, Integer weightType, Integer weight, Integer type) {
		this.keyword = keyword;
		this.weightType = weightType;
		this.weight = weight;
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public Integer getSearchCount() {
		return searchCount;
	}

	public void setSearchCount(Integer searchCount) {
		this.searchCount = searchCount;
	}

	public Integer getSearchResultCount() {
		return searchResultCount;
	}

	public void setSearchResultCount(Integer searchResultCount) {
		this.searchResultCount = searchResultCount;
	}

	public Integer getWeightType() {
		return weightType;
	}

	public void setWeightType(Integer weightType) {
		this.weightType = weightType;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	
}
