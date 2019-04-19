package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.SkuImg;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.sku.image.upload response.
 *
 * @author auto
 * @since 2.0
 */
public class SkuImageUploadResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 图片
	 */
	@ApiField("sku_img")
	private SkuImg skuImg;

	public void setSkuImg(SkuImg skuImg) {
		this.skuImg = skuImg;
	}
	public SkuImg getSkuImg( ) {
		return this.skuImg;
	}

}
