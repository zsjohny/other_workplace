/**
 * 
 */
package com.jiuyuan.entity.search;

import java.io.Serializable;

/**
* @author DongZhong
* @version 创建时间: 2016年9月23日 上午8:48:10
*/
public class AppSearchKeyword implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6970395155987478303L;

	private long id;
	private String keyword;
	private int searchResultHits;
	private int weightType;
	private int weight;
	private long createTime;
	private long updateTime;
	private int guideFlag;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public int getSearchResultHits() {
		return searchResultHits;
	}
	public void setSearchResultHits(int searchResultHits) {
		this.searchResultHits = searchResultHits;
	}
	public int getWeightType() {
		return weightType;
	}
	public void setWeightType(int weightType) {
		this.weightType = weightType;
	}
	public int getWeight() {
		return weight;
	}
	public void setWeight(int weight) {
		this.weight = weight;
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
	public int getGuideFlag() {
		return guideFlag;
	}
	public void setGuideFlag(int guideFlag) {
		this.guideFlag = guideFlag;
	}
	
}
