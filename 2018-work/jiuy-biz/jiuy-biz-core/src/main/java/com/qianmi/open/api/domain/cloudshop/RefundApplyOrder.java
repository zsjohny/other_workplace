package com.qianmi.open.api.domain.cloudshop;

import java.util.List;

import com.qianmi.open.api.QianmiObject;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;

/**
 * 退货退款申请订单详情
 *
 * @author auto
 * @since 2.0
 */
public class RefundApplyOrder extends QianmiObject {

	private static final long serialVersionUID = 1L;

	/**
	 * admin编号
	 */
	@ApiField("admin_id")
	private String adminId;

	/**
	 * 申请凭据
	 */
	@ApiField("apply_credential")
	private String applyCredential;

	/**
	 * 申请单编号
	 */
	@ApiField("apply_id")
	private String applyId;

	/**
	 * 申请单状态 1-待审核 2-已审核通过 3-已收到退货 4-已退款 5-已完成 6-审核未通过
	 */
	@ApiField("apply_state")
	private Integer applyState;

	/**
	 * 申请单创建时间
	 */
	@ApiField("created")
	private String created;

	/**
	 * 申请单完成的时间
	 */
	@ApiField("finished")
	private String finished;

	/**
	 * ITEI码
	 */
	@ApiField("imei")
	private String imei;

	/**
	 * true 成功 false 失败
	 */
	@ApiField("is_success")
	private Boolean isSuccess;

	/**
	 * 会员编号
	 */
	@ApiField("member_id")
	private String memberId;

	/**
	 * 会员昵称
	 */
	@ApiField("member_nick")
	private String memberNick;

	/**
	 * 用户类型 1个人 4经销商
	 */
	@ApiField("member_type")
	private String memberType;

	/**
	 * 是否需要退款：1需要，0不需要
	 */
	@ApiField("need_refund")
	private Integer needRefund;

	/**
	 * 是否需要退货：1需要，0不需要
	 */
	@ApiField("need_return")
	private Integer needReturn;

	/**
	 * 退货单编号
	 */
	@ApiField("receipt_id")
	private String receiptId;

	/**
	 * 实际退款金额
	 */
	@ApiField("refund_actual_amount")
	private String refundActualAmount;

	/**
	 * 申请退款金额
	 */
	@ApiField("refund_apply_amount")
	private String refundApplyAmount;

	/**
	 * 商品列表
	 */
	@ApiListField("refund_apply_items")
	@ApiField("refund_apply_item")
	private List<RefundApplyItem> refundApplyItems;

	/**
	 * 退款单编号
	 */
	@ApiField("refund_id")
	private String refundId;

	/**
	 * 退款原因
	 */
	@ApiField("refund_reason")
	private String refundReason;

	/**
	 * 退款原因说明
	 */
	@ApiField("refund_reason_desc")
	private String refundReasonDesc;

	/**
	 * 商家配送方式名称
	 */
	@ApiField("ship_type_name")
	private String shipTypeName;

	/**
	 * 订单号
	 */
	@ApiField("tid")
	private String tid;

	/**
	 * 上传凭证
	 */
	@ApiField("voucher")
	private String voucher;

	public String getAdminId() {
		return this.adminId;
	}
	public void setAdminId(String adminId) {
		this.adminId = adminId;
	}

	public String getApplyCredential() {
		return this.applyCredential;
	}
	public void setApplyCredential(String applyCredential) {
		this.applyCredential = applyCredential;
	}

	public String getApplyId() {
		return this.applyId;
	}
	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	public Integer getApplyState() {
		return this.applyState;
	}
	public void setApplyState(Integer applyState) {
		this.applyState = applyState;
	}

	public String getCreated() {
		return this.created;
	}
	public void setCreated(String created) {
		this.created = created;
	}

	public String getFinished() {
		return this.finished;
	}
	public void setFinished(String finished) {
		this.finished = finished;
	}

	public String getImei() {
		return this.imei;
	}
	public void setImei(String imei) {
		this.imei = imei;
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

	public String getMemberType() {
		return this.memberType;
	}
	public void setMemberType(String memberType) {
		this.memberType = memberType;
	}

	public Integer getNeedRefund() {
		return this.needRefund;
	}
	public void setNeedRefund(Integer needRefund) {
		this.needRefund = needRefund;
	}

	public Integer getNeedReturn() {
		return this.needReturn;
	}
	public void setNeedReturn(Integer needReturn) {
		this.needReturn = needReturn;
	}

	public String getReceiptId() {
		return this.receiptId;
	}
	public void setReceiptId(String receiptId) {
		this.receiptId = receiptId;
	}

	public String getRefundActualAmount() {
		return this.refundActualAmount;
	}
	public void setRefundActualAmount(String refundActualAmount) {
		this.refundActualAmount = refundActualAmount;
	}

	public String getRefundApplyAmount() {
		return this.refundApplyAmount;
	}
	public void setRefundApplyAmount(String refundApplyAmount) {
		this.refundApplyAmount = refundApplyAmount;
	}

	public List<RefundApplyItem> getRefundApplyItems() {
		return this.refundApplyItems;
	}
	public void setRefundApplyItems(List<RefundApplyItem> refundApplyItems) {
		this.refundApplyItems = refundApplyItems;
	}

	public String getRefundId() {
		return this.refundId;
	}
	public void setRefundId(String refundId) {
		this.refundId = refundId;
	}

	public String getRefundReason() {
		return this.refundReason;
	}
	public void setRefundReason(String refundReason) {
		this.refundReason = refundReason;
	}

	public String getRefundReasonDesc() {
		return this.refundReasonDesc;
	}
	public void setRefundReasonDesc(String refundReasonDesc) {
		this.refundReasonDesc = refundReasonDesc;
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

	public String getVoucher() {
		return this.voucher;
	}
	public void setVoucher(String voucher) {
		this.voucher = voucher;
	}

}