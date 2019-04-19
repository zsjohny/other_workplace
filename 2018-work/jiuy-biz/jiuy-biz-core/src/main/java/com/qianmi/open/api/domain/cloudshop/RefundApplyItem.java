package com.qianmi.open.api.domain.cloudshop;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;

/**
 * 退货退款申请订单商品信息
 *
 * @author auto
 * @since 2.0
 */
public class RefundApplyItem extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 退货退款申请单号
	 */
	@ApiField("apply_id")
	private String applyId;

	/**
	 * 商品购买数量
	 */
	@ApiField("buy_num")
	private String buyNum;

	/**
	 * 成本价，单位元，保留2位小数
	 */
	@ApiField("cost_price")
	private String costPrice;

	/**
	 * 优惠金额，单位元，保留两位小数
	 */
	@ApiField("discount_fee")
	private String discountFee;

	/**
	 * 市场价，单位元，保留2位小数
	 */
	@ApiField("mkt_price")
	private String mktPrice;

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
	 * 对应的商家编码
	 */
	@ApiField("outer_id")
	private String outerId;

	/**
	 * 商品主图地址
	 */
	@ApiField("pic_url")
	private String picUrl;

	/**
	 * 商品价格，单位元，保留两位小数
	 */
	@ApiField("price")
	private String price;

	/**
	 * 交易订单号
	 */
	@ApiField("tid")
	private String tid;

	/**
	 * 商品标题
	 */
	@ApiField("title")
	private String title;

	public String getApplyId() {
		return this.applyId;
	}
	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public String getBuyNum() {
		return this.buyNum;
	}
	public void setBuyNum(String buyNum) {
		this.buyNum = buyNum;
	}

	public String getCostPrice() {
		return this.costPrice;
	}
	public void setCostPrice(String costPrice) {
		this.costPrice = costPrice;
	}

	public String getDiscountFee() {
		return this.discountFee;
	}
	public void setDiscountFee(String discountFee) {
		this.discountFee = discountFee;
	}

	public String getMktPrice() {
		return this.mktPrice;
	}
	public void setMktPrice(String mktPrice) {
		this.mktPrice = mktPrice;
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

	public String getPicUrl() {
		return this.picUrl;
	}
	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getPrice() {
		return this.price;
	}
	public void setPrice(String price) {
		this.price = price;
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