package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.ReceiveOrder;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.d2p.trade.receive.get response.
 *
 * @author auto
 * @since 2.0
 */
public class D2pTradeReceiveGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 收款单信息
	 */
	@ApiListField("receive_orders")
	@ApiField("receive_order")
	private List<ReceiveOrder> receiveOrders;

	public void setReceiveOrders(List<ReceiveOrder> receiveOrders) {
		this.receiveOrders = receiveOrders;
	}
	public List<ReceiveOrder> getReceiveOrders( ) {
		return this.receiveOrders;
	}

}
