package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.Item;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.item.detail.update response.
 *
 * @author auto
 * @since 2.0
 */
public class ItemDetailUpdateResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 返回是否成功is_success
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
