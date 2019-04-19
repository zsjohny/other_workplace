package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.RefundApplyOrder;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.refund.apply.get response.
 *
 * @author auto
 * @since 2.0
 */
public class RefundApplyGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 退货/退款订单详情
	 */
	@ApiField("refund_apply_order")
	private RefundApplyOrder refundApplyOrder;

	public void setRefundApplyOrder(RefundApplyOrder refundApplyOrder) {
		this.refundApplyOrder = refundApplyOrder;
	}
	public RefundApplyOrder getRefundApplyOrder( ) {
		return this.refundApplyOrder;
	}

}
