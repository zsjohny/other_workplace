package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.Sku;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.item.sku.delete response.
 *
 * @author auto
 * @since 2.0
 */
public class ItemSkuDeleteResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 是否成功,只返回is_success字段
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
