package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.Sku;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.item.sku.update response.
 *
 * @author auto
 * @since 2.0
 */
public class ItemSkuUpdateResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 只返回 is_success
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
