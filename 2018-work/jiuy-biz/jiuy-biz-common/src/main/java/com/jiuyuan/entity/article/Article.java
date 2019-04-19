package com.jiuyuan.entity.article;

import java.io.Serializable;

import org.apache.commons.lang3.StringEscapeUtils;

public class Article implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;

	private String title;

	private String content;

	private Integer weight;

	private Long updateTime;

	private Long createTime;
	
	private long aRCategoryId;
	
	private long pageView;
	
	private String abstracts;
	
	private String promotionImg;
	
	private String interfaceTitle;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return StringEscapeUtils.unescapeHtml4(content);
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
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

	public long getaRCategoryId() {
		return aRCategoryId;
	}

	public void setaRCategoryId(long aRCategoryId) {
		this.aRCategoryId = aRCategoryId;
	}

	public long getPageView() {
		return pageView;
	}

	public void setPageView(long pageView) {
		this.pageView = pageView;
	}

	public String getAbstracts() {
		return abstracts;
	}

	public void setAbstracts(String abstracts) {
		this.abstracts = abstracts;
	}

	public String getPromotionImg() {
		return promotionImg;
	}

	public void setPromotionImg(String promotionImg) {
		this.promotionImg = promotionImg;
	}

	public String getInterfaceTitle() {
		return interfaceTitle;
	}

	public void setInterfaceTitle(String interfaceTitle) {
		this.interfaceTitle = interfaceTitle;
	}
	
}
