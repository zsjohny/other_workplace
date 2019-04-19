package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.Sku;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.skus.quantity.batch.update response.
 *
 * @author auto
 * @since 2.0
 */
public class SkusQuantityBatchUpdateResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * sku信息
	 */
	@ApiListField("skus")
	@ApiField("sku")
	private List<Sku> skus;

	public void setSkus(List<Sku> skus) {
		this.skus = skus;
	}
	public List<Sku> getSkus( ) {
		return this.skus;
	}

}
