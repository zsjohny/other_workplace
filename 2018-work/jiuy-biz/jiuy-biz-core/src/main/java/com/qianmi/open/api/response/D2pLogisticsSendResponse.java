package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.Shipping;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.d2p.logistics.send response.
 *
 * @author auto
 * @since 2.0
 */
public class D2pLogisticsSendResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 返回发货是否成功  is_success
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
