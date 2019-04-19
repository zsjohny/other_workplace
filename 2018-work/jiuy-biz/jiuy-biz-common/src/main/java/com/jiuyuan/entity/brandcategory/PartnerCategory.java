package com.jiuyuan.entity.brandcategory;

import java.io.Serializable;

import com.jiuyuan.entity.BaseMeta;

public class PartnerCategory extends BaseMeta<Long> implements Serializable {

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

	// 记录状态，0正常，－1删除
	private Integer status;

	// 记录更新时间
	private Long updateTime;

	// 记录创建时间
	private Long createTime;

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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
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
}
