package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.Shipping;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.d2c.logistics.send response.
 *
 * @author auto
 * @since 2.0
 */
public class D2cLogisticsSendResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 发货结果
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
