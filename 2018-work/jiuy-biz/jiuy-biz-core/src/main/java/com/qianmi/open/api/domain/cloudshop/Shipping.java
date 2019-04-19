package com.qianmi.open.api.domain.cloudshop;

import java.util.List;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;

/**
 * 物流单
 *
 * @author auto
 * @since 2.0
 */
public class Shipping extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 买家会员编号
	 */
	@ApiField("buyer_nick")
	private String buyerNick;

	/**
	 *  买家用户名
	 */
	@ApiField("buyer_nick_name")
	private String buyerNickName;

	/**
	 * 卖家退货地址
	 */
	@ApiField("cancel_address")
	private String cancelAddress;

	/**
	 *  是否发货成功 false失败 true成功
	 */
	@ApiField("is_success")
	private Boolean isSuccess;

	/**
	 * 物流单编号
	 */
	@ApiField("order_code")
	private String orderCode;

	/**
	 *  运单号, 真实的一个物流公司的运单号码
	 */
	@ApiField("out_sid")
	private String outSid;

	/**
	 *  物流费用, 单位:元, 精确到2位小数
	 */
	@ApiField("post_fee")
	private String postFee;

	/**
	 * 详细地址
	 */
	@ApiField("reciver_address")
	private String reciverAddress;

	/**
	 * 收件人所在区县
	 */
	@ApiField("reciver_district")
	private String reciverDistrict;

	/**
	 * 收件人手机
	 */
	@ApiField("reciver_mobile")
	private String reciverMobile;

	/**
	 * 收件人姓名
	 */
	@ApiField("reciver_name")
	private String reciverName;

	/**
	 * 收件人电话
	 */
	@ApiField("reciver_phone")
	private String reciverPhone;

	/**
	 * 收件地址邮编
	 */
	@ApiField("reciver_zip")
	private String reciverZip;

	/**
	 * 卖家编号
	 */
	@ApiField("seller_nick")
	private String sellerNick;

	/**
	 * 发货时间
	 */
	@ApiField("send_time")
	private String sendTime;

	/**
	 * 卖家发货地址
	 */
	@ApiField("sender_address")
	private String senderAddress;

	/**
	 * 物流公司编号
	 */
	@ApiField("ship_company_id")
	private String shipCompanyId;

	/**
	 * 物流公司名称
	 */
	@ApiField("ship_company_name")
	private String shipCompanyName;

	/**
	 * 发货商品清单
	 */
	@ApiListField("ship_items")
	@ApiField("ship_item")
	private List<ShipItem> shipItems;

	/**
	 * 交易单号
	 */
	@ApiField("tid")
	private String tid;

	public String getBuyerNick() {
		return this.buyerNick;
	}
	public void setBuyerNick(String buyerNick) {
		this.buyerNick = buyerNick;
	}

	public String getBuyerNickName() {
		return this.buyerNickName;
	}
	public void setBuyerNickName(String buyerNickName) {
		this.buyerNickName = buyerNickName;
	}

	public String getCancelAddress() {
		return this.cancelAddress;
	}
	public void setCancelAddress(String cancelAddress) {
		this.cancelAddress = cancelAddress;
	}

	public Boolean getIsSuccess() {
		return this.isSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getOrderCode() {
		return this.orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOutSid() {
		return this.outSid;
	}
	public void setOutSid(String outSid) {
		this.outSid = outSid;
	}

	public String getPostFee() {
		return this.postFee;
	}
	public void setPostFee(String postFee) {
		this.postFee = postFee;
	}

	public String getReciverAddress() {
		return this.reciverAddress;
	}
	public void setReciverAddress(String reciverAddress) {
		this.reciverAddress = reciverAddress;
	}

	public String getReciverDistrict() {
		return this.reciverDistrict;
	}
	public void setReciverDistrict(String reciverDistrict) {
		this.reciverDistrict = reciverDistrict;
	}

	public String getReciverMobile() {
		return this.reciverMobile;
	}
	public void setReciverMobile(String reciverMobile) {
		this.reciverMobile = reciverMobile;
	}

	public String getReciverName() {
		return this.reciverName;
	}
	public void setReciverName(String reciverName) {
		this.reciverName = reciverName;
	}

	public String getReciverPhone() {
		return this.reciverPhone;
	}
	public void setReciverPhone(String reciverPhone) {
		this.reciverPhone = reciverPhone;
	}

	public String getReciverZip() {
		return this.reciverZip;
	}
	public void setReciverZip(String reciverZip) {
		this.reciverZip = reciverZip;
	}

	public String getSellerNick() {
		return this.sellerNick;
	}
	public void setSellerNick(String sellerNick) {
		this.sellerNick = sellerNick;
	}

	public String getSendTime() {
		return this.sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}

	public String getSenderAddress() {
		return this.senderAddress;
	}
	public void setSenderAddress(String senderAddress) {
		this.senderAddress = senderAddress;
	}

	public String getShipCompanyId() {
		return this.shipCompanyId;
	}
	public void setShipCompanyId(String shipCompanyId) {
		this.shipCompanyId = shipCompanyId;
	}

	public String getShipCompanyName() {
		return this.shipCompanyName;
	}
	public void setShipCompanyName(String shipCompanyName) {
		this.shipCompanyName = shipCompanyName;
	}

	public List<ShipItem> getShipItems() {
		return this.shipItems;
	}
	public void setShipItems(List<ShipItem> shipItems) {
		this.shipItems = shipItems;
	}

	public String getTid() {
		return this.tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}

}