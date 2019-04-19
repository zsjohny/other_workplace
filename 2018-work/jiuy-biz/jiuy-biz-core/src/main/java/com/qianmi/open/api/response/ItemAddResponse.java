package com.qianmi.open.api.response;

import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.domain.cloudshop.Item;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.item.add response.
 *
 * @author auto
 * @since 2.0
 */
public class ItemAddResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 具体的字段根据请求中设定的fileds决定
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
