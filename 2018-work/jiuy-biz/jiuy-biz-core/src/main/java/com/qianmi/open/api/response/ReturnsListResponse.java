package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.ReturnedOrder;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.returns.list response.
 *
 * @author auto
 * @since 2.0
 */
public class ReturnsListResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 退货单列表
	 */
	@ApiListField("returned_orders")
	@ApiField("returned_order")
	private List<ReturnedOrder> returnedOrders;

	/** 
	 * 总条数
	 */
	@ApiField("total_results")
	private Integer totalResults;

	public void setReturnedOrders(List<ReturnedOrder> returnedOrders) {
		this.returnedOrders = returnedOrders;
	}
	public List<ReturnedOrder> getReturnedOrders( ) {
		return this.returnedOrders;
	}

	public void setTotalResults(Integer totalResults) {
		this.totalResults = totalResults;
	}
	public Integer getTotalResults( ) {
		return this.totalResults;
	}

}
