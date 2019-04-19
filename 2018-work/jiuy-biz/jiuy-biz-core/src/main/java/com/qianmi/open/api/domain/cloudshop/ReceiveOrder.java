package com.qianmi.open.api.domain.cloudshop;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;

/**
 * 收款单信息
 *
 * @author auto
 * @since 2.0
 */
public class ReceiveOrder extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 付款用户编号
	 */
	@ApiField("member_id")
	private String memberId;

	/**
	 * 付款用户名
	 */
	@ApiField("member_nick")
	private String memberNick;

	/**
	 * 操作人
	 */
	@ApiField("operator")
	private String operator;

	/**
	 * 支付帐号
	 */
	@ApiField("pay_account")
	private String payAccount;

	/**
	 * 付款金额
	 */
	@ApiField("pay_amount")
	private String payAmount;

	/**
	 * 付款银行编号
	 */
	@ApiField("pay_bank_code")
	private String payBankCode;

	/**
	 * 付款银行名称
	 */
	@ApiField("pay_bank_name")
	private String payBankName;

	/**
	 * 支付网关编号
	 */
	@ApiField("pay_gate_code")
	private String payGateCode;

	/**
	 * 支付网关名称
	 */
	@ApiField("pay_gate_name")
	private String payGateName;

	/**
	 * 支付单号
	 */
	@ApiField("pay_order_no")
	private String payOrderNo;

	/**
	 * 交易订单支付状态，0 -- 未支付 1 -- 已支付 2 -- 已退款
	 */
	@ApiField("pay_status")
	private Integer payStatus;

	/**
	 * 支付方式编号
	 */
	@ApiField("pay_type_id")
	private String payTypeId;

	/**
	 * 支付方式名称
	 */
	@ApiField("pay_type_name")
	private String payTypeName;

	/**
	 * 付款人
	 */
	@ApiField("pay_user_nick")
	private String payUserNick;

	/**
	 * 手续费金额
	 */
	@ApiField("rate_amount")
	private String rateAmount;

	/**
	 * 收款单据编号
	 */
	@ApiField("receive_id")
	private String receiveId;

	/**
	 * 收款时间
	 */
	@ApiField("receive_time")
	private String receiveTime;

	/**
	 * 备注
	 */
	@ApiField("remark")
	private String remark;

	/**
	 * 订单编号
	 */
	@ApiField("tid")
	private String tid;

	/**
	 * 订单金额
	 */
	@ApiField("trade_amount")
	private String tradeAmount;

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

	public String getOperator() {
		return this.operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getPayAccount() {
		return this.payAccount;
	}
	public void setPayAccount(String payAccount) {
		this.payAccount = payAccount;
	}

	public String getPayAmount() {
		return this.payAmount;
	}
	public void setPayAmount(String payAmount) {
		this.payAmount = payAmount;
	}

	public String getPayBankCode() {
		return this.payBankCode;
	}
	public void setPayBankCode(String payBankCode) {
		this.payBankCode = payBankCode;
	}

	public String getPayBankName() {
		return this.payBankName;
	}
	public void setPayBankName(String payBankName) {
		this.payBankName = payBankName;
	}

	public String getPayGateCode() {
		return this.payGateCode;
	}
	public void setPayGateCode(String payGateCode) {
		this.payGateCode = payGateCode;
	}

	public String getPayGateName() {
		return this.payGateName;
	}
	public void setPayGateName(String payGateName) {
		this.payGateName = payGateName;
	}

	public String getPayOrderNo() {
		return this.payOrderNo;
	}
	public void setPayOrderNo(String payOrderNo) {
		this.payOrderNo = payOrderNo;
	}

	public Integer getPayStatus() {
		return this.payStatus;
	}
	public void setPayStatus(Integer payStatus) {
		this.payStatus = payStatus;
	}

	public String getPayTypeId() {
		return this.payTypeId;
	}
	public void setPayTypeId(String payTypeId) {
		this.payTypeId = payTypeId;
	}

	public String getPayTypeName() {
		return this.payTypeName;
	}
	public void setPayTypeName(String payTypeName) {
		this.payTypeName = payTypeName;
	}

	public String getPayUserNick() {
		return this.payUserNick;
	}
	public void setPayUserNick(String payUserNick) {
		this.payUserNick = payUserNick;
	}

	public String getRateAmount() {
		return this.rateAmount;
	}
	public void setRateAmount(String rateAmount) {
		this.rateAmount = rateAmount;
	}

	public String getReceiveId() {
		return this.receiveId;
	}
	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}

	public String getReceiveTime() {
		return this.receiveTime;
	}
	public void setReceiveTime(String receiveTime) {
		this.receiveTime = receiveTime;
	}

	public String getRemark() {
		return this.remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getTid() {
		return this.tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}

	public String getTradeAmount() {
		return this.tradeAmount;
	}
	public void setTradeAmount(String tradeAmount) {
		this.tradeAmount = tradeAmount;
	}

}