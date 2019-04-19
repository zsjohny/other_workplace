package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.RefundOrder;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.refund.get response.
 *
 * @author auto
 * @since 2.0
 */
public class RefundGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 退款单详情
	 */
	@ApiField("refund_order")
	private RefundOrder refundOrder;

	public void setRefundOrder(RefundOrder refundOrder) {
		this.refundOrder = refundOrder;
	}
	public RefundOrder getRefundOrder( ) {
		return this.refundOrder;
	}

}
