package com.qianmi.open.api.domain.cloudshop;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;

/**
 * 商品仓库商品类目
 *
 * @author auto
 * @since 2.0
 */
public class ItemCat extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 类目id
	 */
	@ApiField("cid")
	private String cid;

	/**
	 * 类目深度
	 */
	@ApiField("depth")
	private Integer depth;

	/**
	 * 是否是叶子类目 0：否 1：是
	 */
	@ApiField("is_leaf")
	private Integer isLeaf;

	/**
	 * 是否成功
	 */
	@ApiField("is_success")
	private Boolean isSuccess;

	/**
	 * 类目名称
	 */
	@ApiField("name")
	private String name;

	/**
	 * 排序
	 */
	@ApiField("order")
	private Integer order;

	/**
	 * 父类目id
	 */
	@ApiField("parent_cid")
	private String parentCid;

	/**
	 * 类目路径
	 */
	@ApiField("path")
	private String path;

	/**
	 * 搜索时的目录价格区间，json格式
	 */
	@ApiField("price_range")
	private String priceRange;

	/**
	 * 启用状态 0：否 1：是
	 */
	@ApiField("status")
	private Integer status;

	public String getCid() {
		return this.cid;
	}
	public void setCid(String cid) {
		this.cid = cid;
	}

	public Integer getDepth() {
		return this.depth;
	}
	public void setDepth(Integer depth) {
		this.depth = depth;
	}

	public Integer getIsLeaf() {
		return this.isLeaf;
	}
	public void setIsLeaf(Integer isLeaf) {
		this.isLeaf = isLeaf;
	}

	public Boolean getIsSuccess() {
		return this.isSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getName() {
		return this.name;
	}
	public void setName(String name) {
		this.name = name;
	}

	public Integer getOrder() {
		return this.order;
	}
	public void setOrder(Integer order) {
		this.order = order;
	}

	public String getParentCid() {
		return this.parentCid;
	}
	public void setParentCid(String parentCid) {
		this.parentCid = parentCid;
	}

	public String getPath() {
		return this.path;
	}
	public void setPath(String path) {
		this.path = path;
	}

	public String getPriceRange() {
		return this.priceRange;
	}
	public void setPriceRange(String priceRange) {
		this.priceRange = priceRange;
	}

	public Integer getStatus() {
		return this.status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "ItemCat [cid=" + cid + ", depth=" + depth + ", isLeaf=" + isLeaf + ", isSuccess=" + isSuccess
				+ ", name=" + name + ", order=" + order + ", parentCid=" + parentCid + ", path=" + path
				+ ", priceRange=" + priceRange + ", status=" + status + "]";
	}
	
	

}