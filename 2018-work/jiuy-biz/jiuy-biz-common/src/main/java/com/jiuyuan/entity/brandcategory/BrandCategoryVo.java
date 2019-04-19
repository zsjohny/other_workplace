package com.jiuyuan.entity.brandcategory;

import java.io.Serializable;

public class BrandCategoryVo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// 主键ID
	private Long id;

	// 商家ID
	private Long partnerId;

	// 分类ID
	private Long categoryId;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPartnerId() {
		return partnerId;
	}

	public void setPartnerId(Long partnerId) {
		this.partnerId = partnerId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	@Override
	public String toString() {
		return "BrandCategoryVo [id=" + id + ", partnerId=" + partnerId + ", categoryId=" + categoryId + "]";
	}
	
	

}
