package com.qianmi.open.api.domain.cloudshop;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;

/**
 * 退款单
 *
 * @author auto
 * @since 2.0
 */
public class RefundOrder extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * 退款单创建时间
	 */
	@ApiField("created")
	private String created;

	/**
	 * 是否成功
	 */
	@ApiField("is_success")
	private Boolean isSuccess;

	/**
	 * 会员编号(退款用户编号)
	 */
	@ApiField("member_id")
	private String memberId;

	/**
	 * 会员昵称(退款用户名)
	 */
	@ApiField("member_nick")
	private String memberNick;

	/**
	 * 操作人
	 */
	@ApiField("operator")
	private String operator;

	/**
	 * 外部订单号
	 */
	@ApiField("out_order_no")
	private String outOrderNo;

	/**
	 * 订单支付方式
	 */
	@ApiField("pay_type_id")
	private String payTypeId;

	/**
	 * 订单支付方式名称
	 */
	@ApiField("pay_type_name")
	private String payTypeName;

	/**
	 * 退款帐号
	 */
	@ApiField("refund_account")
	private String refundAccount;

	/**
	 * 退款帐号户名
	 */
	@ApiField("refund_account_name")
	private String refundAccountName;

	/**
	 * 退款金额
	 */
	@ApiField("refund_amount")
	private String refundAmount;

	/**
	 * 退款银行编号
	 */
	@ApiField("refund_bank_code")
	private String refundBankCode;

	/**
	 * 退款银行名称
	 */
	@ApiField("refund_bank_name")
	private String refundBankName;

	/**
	 * 退款支付网关编号
	 */
	@ApiField("refund_gate_code")
	private String refundGateCode;

	/**
	 * 退款支付网关名称
	 */
	@ApiField("refund_gate_name")
	private String refundGateName;

	/**
	 * 退款单号
	 */
	@ApiField("refund_id")
	private String refundId;

	/**
	 * 退款备注
	 */
	@ApiField("refund_remark")
	private String refundRemark;

	/**
	 * 退款状态 0-未处理 1-退款处理完毕
	 */
	@ApiField("refund_state")
	private Integer refundState;

	/**
	 * 退款类型 0-售中 1-售后
	 */
	@ApiField("refund_type")
	private Integer refundType;

	/**
	 * 退款方式编号
	 */
	@ApiField("refund_type_id")
	private String refundTypeId;

	/**
	 * 退款方式名称
	 */
	@ApiField("refund_type_name")
	private String refundTypeName;

	/**
	 * 订单编号
	 */
	@ApiField("tid")
	private String tid;

	public String getCreated() {
		return this.created;
	}
	public void setCreated(String created) {
		this.created = created;
	}

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

	public String getOperator() {
		return this.operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getOutOrderNo() {
		return this.outOrderNo;
	}
	public void setOutOrderNo(String outOrderNo) {
		this.outOrderNo = outOrderNo;
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

	public String getRefundAccount() {
		return this.refundAccount;
	}
	public void setRefundAccount(String refundAccount) {
		this.refundAccount = refundAccount;
	}

	public String getRefundAccountName() {
		return this.refundAccountName;
	}
	public void setRefundAccountName(String refundAccountName) {
		this.refundAccountName = refundAccountName;
	}

	public String getRefundAmount() {
		return this.refundAmount;
	}
	public void setRefundAmount(String refundAmount) {
		this.refundAmount = refundAmount;
	}

	public String getRefundBankCode() {
		return this.refundBankCode;
	}
	public void setRefundBankCode(String refundBankCode) {
		this.refundBankCode = refundBankCode;
	}

	public String getRefundBankName() {
		return this.refundBankName;
	}
	public void setRefundBankName(String refundBankName) {
		this.refundBankName = refundBankName;
	}

	public String getRefundGateCode() {
		return this.refundGateCode;
	}
	public void setRefundGateCode(String refundGateCode) {
		this.refundGateCode = refundGateCode;
	}

	public String getRefundGateName() {
		return this.refundGateName;
	}
	public void setRefundGateName(String refundGateName) {
		this.refundGateName = refundGateName;
	}

	public String getRefundId() {
		return this.refundId;
	}
	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}

	public String getRefundRemark() {
		return this.refundRemark;
	}
	public void setRefundRemark(String refundRemark) {
		this.refundRemark = refundRemark;
	}

	public Integer getRefundState() {
		return this.refundState;
	}
	public void setRefundState(Integer refundState) {
		this.refundState = refundState;
	}

	public Integer getRefundType() {
		return this.refundType;
	}
	public void setRefundType(Integer refundType) {
		this.refundType = refundType;
	}

	public String getRefundTypeId() {
		return this.refundTypeId;
	}
	public void setRefundTypeId(String refundTypeId) {
		this.refundTypeId = refundTypeId;
	}

	public String getRefundTypeName() {
		return this.refundTypeName;
	}
	public void setRefundTypeName(String refundTypeName) {
		this.refundTypeName = refundTypeName;
	}

	public String getTid() {
		return this.tid;
	}
	public void setTid(String tid) {
		this.tid = tid;
	}

}