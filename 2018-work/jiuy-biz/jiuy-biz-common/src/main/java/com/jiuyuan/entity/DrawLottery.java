package com.jiuyuan.entity;

import java.io.Serializable;

/**
 * @author jeff.zhan
 * @version 2016年11月3日 下午7:42:26
 * 
 */

public class DrawLottery implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 8352899578406610601L;

	private Long id;
	
	private String name;
	
	private String rankName;
	
	private Integer type;
	
	private Integer jiuCoin;
	
	private Long relatedId;
	
	private String image;
	
	private Integer count;
	
	private Integer weight;
	
	private Integer adjustStatus;
	
	private Integer adjustType;
	
	private Long adjustTime;
	
	private Integer adjustCount;
	
	private Integer percent;
	
	private Long lastAdjustTime;
	
	private Long productId;
	
	private Integer status;
	
	private Long createTime;
	
	private Long updateTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRankName() {
		return rankName;
	}

	public void setRankName(String rankName) {
		this.rankName = rankName;
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

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	public Integer getAdjustStatus() {
		return adjustStatus;
	}

	public void setAdjustStatus(Integer adjustStatus) {
		this.adjustStatus = adjustStatus;
	}

	public Integer getAdjustType() {
		return adjustType;
	}

	public void setAdjustType(Integer adjustType) {
		this.adjustType = adjustType;
	}

	public Long getAdjustTime() {
		return adjustTime;
	}

	public void setAdjustTime(Long adjustTime) {
		this.adjustTime = adjustTime;
	}

	public Integer getAdjustCount() {
		return adjustCount;
	}

	public void setAdjustCount(Integer adjustCount) {
		this.adjustCount = adjustCount;
	}

	public Integer getPercent() {
		return percent;
	}

	public void setPercent(Integer percent) {
		this.percent = percent;
	}
	
	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Long getLastAdjustTime() {
		return lastAdjustTime;
	}

	public void setLastAdjustTime(Long lastAdjustTime) {
		this.lastAdjustTime = lastAdjustTime;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
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

	public Integer getJiuCoin() {
		return jiuCoin;
	}

	public void setJiuCoin(Integer jiuCoin) {
		this.jiuCoin = jiuCoin;
	}
	
}
