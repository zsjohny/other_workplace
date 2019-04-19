package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.Shop;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.shop.get response.
 *
 * @author auto
 * @since 2.0
 */
public class ShopGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * null
	 */
	@ApiField("shop")
	private Shop shop;

	public void setShop(Shop shop) {
		this.shop = shop;
	}
	public Shop getShop( ) {
		return this.shop;
	}

}
