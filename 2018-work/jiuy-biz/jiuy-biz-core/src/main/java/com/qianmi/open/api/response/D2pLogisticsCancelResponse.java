package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.Shipping;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.d2p.logistics.cancel response.
 *
 * @author auto
 * @since 2.0
 */
public class D2pLogisticsCancelResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 返回是否成功is_success
	 */
	@ApiField("shipping")
	private Shipping shipping;

	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}
	public Shipping getShipping( ) {
		return this.shipping;
	}

}
