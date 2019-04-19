package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.Sku;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.item.sku.price.update response.
 *
 * @author auto
 * @since 2.0
 */
public class ItemSkuPriceUpdateResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 货品信息
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
