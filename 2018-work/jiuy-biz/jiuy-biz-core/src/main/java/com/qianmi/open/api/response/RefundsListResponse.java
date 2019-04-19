package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.RefundOrder;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.refunds.list response.
 *
 * @author auto
 * @since 2.0
 */
public class RefundsListResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 退款单列表
	 */
	@ApiListField("refund_orders")
	@ApiField("refund_order")
	private List<RefundOrder> refundOrders;

	/** 
	 * 总条数
	 */
	@ApiField("total_results")
	private Integer totalResults;

	public void setRefundOrders(List<RefundOrder> refundOrders) {
		this.refundOrders = refundOrders;
	}
	public List<RefundOrder> getRefundOrders( ) {
		return this.refundOrders;
	}

	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}
	public Integer getTotalResults( ) {
		return this.totalResults;
	}

}
