package com.qianmi.open.api.domain.cloudshop;

import java.util.List;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;

/**
 * 订单信息
 *
 * @author auto
 * @since 2.0
 */
public class Order extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 商品品牌
	 */
	@ApiField("brand_name")
	private String brandName;

	/**
	 * 买家会员编号
	 */
	@ApiField("buyer_nick")
	private String buyerNick;

	/**
	 * 优惠金额, 单位:元, 2位小数
	 */
	@ApiField("discount_fee")
	private String discountFee;

	/**
	 * 营销活动信息
	 */
	@ApiListField("marketing_records")
	@ApiField("marketing_record")
	private List<MarketingRecord> marketingRecords;

	/**
	 * 购买数量, 大于0的整数
	 */
	@ApiField("num")
	private Integer num;

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
	 * 商品的商家编码
	 */
	@ApiField("outer_id")
	private String outerId;

	/**
	 * 商家外部SKU编号
	 */
	@ApiField("outer_sku_id")
	private String outerSkuId;

	/**
	 * 实际支付金额, 单位:元, 2位小数
	 */
	@ApiField("payment")
	private String payment;

	/**
	 * 商品图片地址
	 */
	@ApiField("pic_path")
	private String picPath;

	/**
	 *  商品成交单价, 单位:元, 2位小数
	 */
	@ApiField("price")
	private String price;

	/**
	 *  商品原始售价, 单位:元, 2位小数
	 */
	@ApiField("raw_price")
	private String rawPrice;

	/**
	 * 卖家编号
	 */
	@ApiField("seller_nick")
	private String sellerNick;

	/**
	 *  千米网卖家类型admin、retail
	 */
	@ApiField("seller_type")
	private String sellerType;

	/**
	 * SKU编号
	 */
	@ApiField("sku_id")
	private String skuId;

	/**
	 * 商品规格
	 */
	@ApiField("sku_properties_name")
	private String skuPropertiesName;

	/**
	 * 商品标题
	 */
	@ApiField("title")
	private String title;

	/**
	 * 应付金额, 单位:元, 2位小数
	 */
	@ApiField("total_fee")
	private String totalFee;

	/**
	 * 商品单位
	 */
	@ApiField("unit")
	private String unit;

	/**
	 * 成本单价, 单位:元, 2位小数
	 */
	@ApiField("unit_cost")
	private String unitCost;

	public String getBrandName() {
		return this.brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}

	public String getBuyerNick() {
		return this.buyerNick;
	}
	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	public String getDiscountFee() {
		return this.discountFee;
	}
	public void setDiscountFee(String discountFee) {
		this.discountFee = discountFee;
	}

	public List<MarketingRecord> getMarketingRecords() {
		return this.marketingRecords;
	}
	public void setMarketingRecords(List<MarketingRecord> marketingRecords) {
		this.marketingRecords = marketingRecords;
	}

	public Integer getNum() {
		return this.num;
	}
	public void setNum(Integer num) {
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

	public String getOuterId() {
		return this.outerId;
	}
	public void setOuterId(String outerId) {
		this.outerId = outerId;
	}

	public String getOuterSkuId() {
		return this.outerSkuId;
	}
	public void setOuterSkuId(String outerSkuId) {
		this.outerSkuId = outerSkuId;
	}

	public String getPayment() {
		return this.payment;
	}
	public void setPayment(String payment) {
		this.payment = payment;
	}

	public String getPicPath() {
		return this.picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getPrice() {
		return this.price;
	}
	public void setPrice(String price) {
		this.price = price;
	}

	public String getRawPrice() {
		return this.rawPrice;
	}
	public void setRawPrice(String rawPrice) {
		this.rawPrice = rawPrice;
	}

	public String getSellerNick() {
		return this.sellerNick;
	}
	public void setSellerNick(String sellerNick) {
		this.sellerNick = sellerNick;
	}

	public String getSellerType() {
		return this.sellerType;
	}
	public void setSellerType(String sellerType) {
		this.sellerType = sellerType;
	}

	public String getSkuId() {
		return this.skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getSkuPropertiesName() {
		return this.skuPropertiesName;
	}
	public void setSkuPropertiesName(String skuPropertiesName) {
		this.skuPropertiesName = skuPropertiesName;
	}

	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title = title;
	}

	public String getTotalFee() {
		return this.totalFee;
	}
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getUnit() {
		return this.unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getUnitCost() {
		return this.unitCost;
	}
	public void setUnitCost(String unitCost) {
		this.unitCost = unitCost;
	}

}