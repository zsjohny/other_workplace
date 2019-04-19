package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.RefundApplyOrder;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.refunds.apply.list response.
 *
 * @author auto
 * @since 2.0
 */
public class RefundsApplyListResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 退货/退款申请订单列表
	 */
	@ApiListField("refund_apply_orders")
	@ApiField("refund_apply_order")
	private List<RefundApplyOrder> refundApplyOrders;

	/** 
	 * 总记录数
	 */
	@ApiField("total_results")
	private Integer totalResults;

	public void setRefundApplyOrders(List<RefundApplyOrder> refundApplyOrders) {
		this.refundApplyOrders = refundApplyOrders;
	}
	public List<RefundApplyOrder> getRefundApplyOrders( ) {
		return this.refundApplyOrders;
	}

	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}
	public Integer getTotalResults( ) {
		return this.totalResults;
	}

}
