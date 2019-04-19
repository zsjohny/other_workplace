package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.ReturnedOrder;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.return.get response.
 *
 * @author auto
 * @since 2.0
 */
public class ReturnGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 退货单列表
	 */
	@ApiField("returned_order")
	private ReturnedOrder returnedOrder;

	public void setReturnedOrder(ReturnedOrder returnedOrder) {
		this.returnedOrder = returnedOrder;
	}
	public ReturnedOrder getReturnedOrder( ) {
		return this.returnedOrder;
	}

}
