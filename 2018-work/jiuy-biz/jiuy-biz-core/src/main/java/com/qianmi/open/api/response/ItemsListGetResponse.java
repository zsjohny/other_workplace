package com.qianmi.open.api.response;

import java.util.List;
import com.qianmi.open.api.tool.mapping.ApiField;
import com.qianmi.open.api.tool.mapping.ApiListField;
import com.qianmi.open.api.domain.cloudshop.Item;

import com.qianmi.open.api.QianmiResponse;

/**
 * API: qianmi.cloudshop.items.list.get response.
 *
 * @author auto
 * @since 2.0
 */
public class ItemsListGetResponse extends QianmiResponse {

	private static final long serialVersionUID = 1L;

	/** 
	 * 商品信息列表
	 */
	@ApiListField("items")
	@ApiField("item")
	private List<Item> items;

	public void setItems(List<Item> items) {
		this.items = items;
	}
	public List<Item> getItems( ) {
		return this.items;
	}

}
