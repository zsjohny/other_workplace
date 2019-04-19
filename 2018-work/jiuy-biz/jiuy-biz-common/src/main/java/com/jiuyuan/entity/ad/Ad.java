package com.jiuyuan.entity.ad;

import java.io.Serializable;

import com.jiuyuan.entity.BaseMeta;


public class Ad extends BaseMeta<Long> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private Integer adType;

	private Integer newPage;

	private Integer adOrder;

	private String imageUrl;

	private String linkUrl;

	private Long updateTime;

	private Long createTime;
	
	private String adTitle;

	private Long partenerId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getAdType() {
		return adType;
	}

	public void setAdType(Integer adType) {
		this.adType = adType;
	}

	public Integer getAdOrder() {
		return adOrder;
	}

	public void setAdOrder(Integer adOrder) {
		this.adOrder = adOrder;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
	}

	public Long getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}

	public Long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Long createTime) {
		this.createTime = createTime;
	}

	@Override
	public Long getCacheId() {
		return id;
	}

	public Integer getNewPage() {
		return newPage;
	}

	public void setNewPage(Integer newPage) {
		this.newPage = newPage;
	}

	public String getAdTitle() {
		return adTitle;
	}

	public void setAdTitle(String adTitle) {
		this.adTitle = adTitle;
	}

	public Long getPartenerId() {
		return partenerId;
	}

	public void setPartenerId(Long partenerId) {
		this.partenerId = partenerId;
	}
	

}
