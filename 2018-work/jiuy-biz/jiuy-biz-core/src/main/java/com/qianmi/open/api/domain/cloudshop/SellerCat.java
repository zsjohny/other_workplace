package com.qianmi.open.api.domain.cloudshop;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;

/**
 * 卖家自定义类目
 *
 * @author auto
 * @since 2.0
 */
public class SellerCat extends QianmiObject {

	private static final long serialVersionUID = 1L;

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
	 * 展示类目名称
	 */
	@ApiField("name")
	private String name;

	/**
	 * 排序
	 */
	@ApiField("order")
	private Integer order;

	/**
	 * 展示类目父id
	 */
	@ApiField("p_seller_cid")
	private String pSellerCid;

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
	 * 商品所属展示类目id
	 */
	@ApiField("seller_cid")
	private String sellerCid;

	/**
	 * 产品线 1: 云订货(D2P) 2: 云商城(D2C)
	 */
	@ApiField("site")
	private String site;

	/**
	 * 启用状态 0：否 1：是
	 */
	@ApiField("status")
	private Integer status;

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

	public String getpSellerCid() {
		return this.pSellerCid;
	}
	public void setpSellerCid(String pSellerCid) {
		this.pSellerCid = pSellerCid;
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

	public String getSellerCid() {
		return this.sellerCid;
	}
	public void setSellerCid(String sellerCid) {
		this.sellerCid = sellerCid;
	}

	public String getSite() {
		return this.site;
	}
	public void setSite(String site) {
		this.site = site;
	}

	public Integer getStatus() {
		return this.status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		return "SellerCat [depth=" + depth + ", isLeaf=" + isLeaf + ", isSuccess=" + isSuccess + ", name=" + name
				+ ", order=" + order + ", pSellerCid=" + pSellerCid + ", path=" + path + ", priceRange=" + priceRange
				+ ", sellerCid=" + sellerCid + ", site=" + site + ", status=" + status + "]";
	}
	
	

}