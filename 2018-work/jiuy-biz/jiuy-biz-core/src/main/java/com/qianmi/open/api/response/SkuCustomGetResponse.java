package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.Sku;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.sku.custom.get response.
 *
 * @author auto
 * @since 2.0
 */
public class SkuCustomGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * sku详情
	 */
	@ApiField("sku")
	private Sku sku;

	public void setSku(Sku sku) {
		this.sku = sku;
	}
	public Sku getSku( ) {
		return this.sku;
	}

}
