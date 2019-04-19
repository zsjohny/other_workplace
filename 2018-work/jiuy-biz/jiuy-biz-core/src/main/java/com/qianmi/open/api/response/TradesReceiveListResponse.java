package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.ReceiveOrder;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.trades.receive.list response.
 *
 * @author auto
 * @since 2.0
 */
public class TradesReceiveListResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 收款单列表
	 */
	@ApiListField("receive_orders")
	@ApiField("receive_order")
	private List<ReceiveOrder> receiveOrders;

	/** 
	 * 总条数
	 */
	@ApiField("total_results")
	private Integer totalResults;

	public void setReceiveOrders(List<ReceiveOrder> receiveOrders) {
		this.receiveOrders = receiveOrders;
	}
	public List<ReceiveOrder> getReceiveOrders( ) {
		return this.receiveOrders;
	}

	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}
	public Integer getTotalResults( ) {
		return this.totalResults;
	}

}
