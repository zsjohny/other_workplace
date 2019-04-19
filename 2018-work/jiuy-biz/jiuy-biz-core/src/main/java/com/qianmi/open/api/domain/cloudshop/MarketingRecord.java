package com.qianmi.open.api.domain.cloudshop;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;

/**
 * 营销活动信息
 *
 * @author auto
 * @since 2.0
 */
public class MarketingRecord extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 当前优惠金额
	 */
	@ApiField("cur_discount_fee")
	private String curDiscountFee;

	/**
	 * 初始优惠金额
	 */
	@ApiField("init_discount_fee")
	private String initDiscountFee;

	/**
	 * 是否赠品，0：否，1：是
	 */
	@ApiField("is_gift")
	private Integer isGift;

	/**
	 * 是否优惠套装，0：否，1：是
	 */
	@ApiField("is_package")
	private Integer isPackage;

	/**
	 * 是否优惠套装主体，0：否，1：是
	 */
	@ApiField("is_package_subject")
	private Integer isPackageSubject;

	/**
	 * 活动描述
	 */
	@ApiField("marketing_desc")
	private String marketingDesc;

	/**
	 * 活动编号
	 */
	@ApiField("marketing_id")
	private String marketingId;

	/**
	 * 活动名称
	 */
	@ApiField("marketing_name")
	private String marketingName;

	/**
	 * 活动类型
	 */
	@ApiField("marketing_type")
	private String marketingType;

	/**
	 * 商品数量
	 */
	@ApiField("num")
	private String num;

	/**
	 * 商品编号
	 */
	@ApiField("num_iid")
	private String numIid;

	/**
	 * 商品单编号
	 */
	@ApiField("oid")
	private String oid;

	/**
	 * 优惠套装编号
	 */
	@ApiField("package_id")
	private String packageId;

	/**
	 * 优惠套装名称
	 */
	@ApiField("package_name")
	private String packageName;

	/**
	 * 订单编号
	 */
	@ApiField("tid")
	private String tid;

	/**
	 * 商品标题
	 */
	@ApiField("title")
	private String title;

	public String getCurDiscountFee() {
		return this.curDiscountFee;
	}
	public void setCurDiscountFee(String curDiscountFee) {
		this.curDiscountFee = curDiscountFee;
	}

	public String getInitDiscountFee() {
		return this.initDiscountFee;
	}
	public void setInitDiscountFee(String initDiscountFee) {
		this.initDiscountFee = initDiscountFee;
	}

	public Integer getIsGift() {
		return this.isGift;
	}
	public void setIsGift(Integer isGift) {
		this.isGift = isGift;
	}

	public Integer getIsPackage() {
		return this.isPackage;
	}
	public void setIsPackage(Integer isPackage) {
		this.isPackage = isPackage;
	}

	public Integer getIsPackageSubject() {
		return this.isPackageSubject;
	}
	public void setIsPackageSubject(Integer isPackageSubject) {
		this.isPackageSubject = isPackageSubject;
	}

	public String getMarketingDesc() {
		return this.marketingDesc;
	}
	public void setMarketingDesc(String marketingDesc) {
		this.marketingDesc = marketingDesc;
	}

	public String getMarketingId() {
		return this.marketingId;
	}
	public void setMarketingId(String marketingId) {
		this.marketingId = marketingId;
	}

	public String getMarketingName() {
		return this.marketingName;
	}
	public void setMarketingName(String marketingName) {
		this.marketingName = marketingName;
	}

	public String getMarketingType() {
		return this.marketingType;
	}
	public void setMarketingType(String marketingType) {
		this.marketingType = marketingType;
	}

	public String getNum() {
		return this.num;
	}
	public void setNum(String num) {
		this.num = num;
	}

	public String getNumIid() {
		return this.numIid;
	}
	public void setNumIid(String numIid) {
		this.numIid = numIid;
	}

	public String getOid() {
		return this.oid;
	}
	public void setOid(String oid) {
		this.oid = oid;
	}

	public String getPackageId() {
		return this.packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getPackageName() {
		return this.packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getTid() {
		return this.tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

}