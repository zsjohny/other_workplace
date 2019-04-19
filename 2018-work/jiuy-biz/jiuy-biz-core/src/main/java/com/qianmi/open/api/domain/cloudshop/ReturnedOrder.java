package com.qianmi.open.api.domain.cloudshop;

import java.util.List;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;

/**
 * 退货单详情信息
 *
 * @author auto
 * @since 2.0
 */
public class ReturnedOrder extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * true 成功 false 失败
	 */
	@ApiField("is_success")
	private Boolean isSuccess;

	/**
	 * 买家编号
	 */
	@ApiField("member_id")
	private String memberId;

	/**
	 * 买家用户名
	 */
	@ApiField("member_nick")
	private String memberNick;

	/**
	 * 外部订单号
	 */
	@ApiField("out_order")
	private String outOrder;

	/**
	 * 运单号，真实的一个物流公司的运单号码
	 */
	@ApiField("out_sid")
	private String outSid;

	/**
	 * 物流费用
	 */
	@ApiField("post_fee")
	private String postFee;

	/**
	 * 收件人详细地址
	 */
	@ApiField("reciver_address")
	private String reciverAddress;

	/**
	 * 收件人所在县区
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
	 * 收件人地址邮编
	 */
	@ApiField("reciver_zip")
	private String reciverZip;

	/**
	 * 退货单号
	 */
	@ApiField("return_id")
	private String returnId;

	/**
	 * 退货商品清单
	 */
	@ApiListField("return_items")
	@ApiField("returned_item")
	private List<ReturnedItem> returnItems;

	/**
	 * 退货原因编号  01-拒收 02-换货 03-质量原因 04-发错货 05-无理由
	 */
	@ApiField("return_reason_id")
	private String returnReasonId;

	/**
	 * 退货原因名称
	 */
	@ApiField("return_reason_name")
	private String returnReasonName;

	/**
	 * 退货时间
	 */
	@ApiField("return_time")
	private String returnTime;

	/**
	 * 退货类型
	 */
	@ApiField("return_type")
	private Integer returnType;

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
	 * 退货配送方式编号
	 */
	@ApiField("ship_type_id")
	private String shipTypeId;

	/**
	 * 退货配送方式名称
	 */
	@ApiField("ship_type_name")
	private String shipTypeName;

	/**
	 * 交易单号
	 */
	@ApiField("tid")
	private String tid;

	public Boolean getIsSuccess() {
		return this.isSuccess;
	}
	public void setIsSuccess(Boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public String getMemberId() {
		return this.memberId;
	}
	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberNick() {
		return this.memberNick;
	}
	public void setMemberNick(String memberNick) {
		this.memberNick = memberNick;
	}

	public String getOutOrder() {
		return this.outOrder;
	}
	public void setOutOrder(String outOrder) {
		this.outOrder = outOrder;
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

	public String getReturnId() {
		return this.returnId;
	}
	public void setReturnId(String returnId) {
		this.returnId = returnId;
	}

	public List<ReturnedItem> getReturnItems() {
		return this.returnItems;
	}
	public void setReturnItems(List<ReturnedItem> returnItems) {
		this.returnItems = returnItems;
	}

	public String getReturnReasonId() {
		return this.returnReasonId;
	}
	public void setReturnReasonId(String returnReasonId) {
		this.returnReasonId = returnReasonId;
	}

	public String getReturnReasonName() {
		return this.returnReasonName;
	}
	public void setReturnReasonName(String returnReasonName) {
		this.returnReasonName = returnReasonName;
	}

	public String getReturnTime() {
		return this.returnTime;
	}
	public void setReturnTime(String returnTime) {
		this.returnTime = returnTime;
	}

	public Integer getReturnType() {
		return this.returnType;
	}
	public void setReturnType(Integer returnType) {
		this.returnType = returnType;
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

	public String getShipTypeId() {
		return this.shipTypeId;
	}
	public void setShipTypeId(String shipTypeId) {
		this.shipTypeId = shipTypeId;
	}

	public String getShipTypeName() {
		return this.shipTypeName;
	}
	public void setShipTypeName(String shipTypeName) {
		this.shipTypeName = shipTypeName;
	}

	public String getTid() {
		return this.tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}

}