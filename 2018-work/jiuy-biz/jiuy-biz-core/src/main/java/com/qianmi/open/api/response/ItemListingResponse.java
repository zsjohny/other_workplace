package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.Item;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.item.listing response.
 *
 * @author auto
 * @since 2.0
 */
public class ItemListingResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 是否成功，只返回商品的is_success字段
	 */
	@ApiField("item")
	private Item item;

	public void setItem(Item item) {
		this.item = item;
	}
	public Item getItem( ) {
		return this.item;
	}

}
